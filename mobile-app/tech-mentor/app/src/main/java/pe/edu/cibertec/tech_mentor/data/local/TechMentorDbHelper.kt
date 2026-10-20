package pe.edu.cibertec.tech_mentor.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile
import pe.edu.cibertec.tech_mentor.ui.categories.CourseCategory

/**
 * Base de datos SQLite local para persistencia sin conexión (Offline-first)
 * Enfocada exclusivamente en la persistencia de Inicio (Home) y Categorías.
 */
class TechMentorDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TAG = "TechMentorDbHelper"
        private const val DATABASE_NAME = "techmentor_local.db"
        private const val DATABASE_VERSION = 1

        // Tabla Categorias
        private const val TABLE_CATEGORIES = "local_categories"
        private const val COL_CAT_ID = "id_categoria"
        private const val COL_CAT_NOMBRE = "nombre"
        private const val COL_CAT_DESCRIPCION = "descripcion"
        private const val COL_CAT_COUNT = "cursos_count"

        // Tabla Cursos
        private const val TABLE_COURSES = "local_courses"
        private const val COL_CRS_ID = "id_curso"
        private const val COL_CRS_CAT_ID = "id_categoria"
        private const val COL_CRS_NOMBRE = "nombre"
        private const val COL_CRS_DESCRIPCION = "descripcion"
        private const val COL_CRS_ICONO = "icono"
        private const val COL_CRS_XP_REQ = "xp_requerido"
        private const val COL_CRS_XP_REC = "xp_recompensa"
        private const val COL_CRS_ACTIVO = "activo"

        // Tabla Perfil de Usuario
        private const val TABLE_PROFILE = "local_profile"
        private const val COL_PRF_ID = "id_usuario"
        private const val COL_PRF_NOMBRES = "nombres"
        private const val COL_PRF_APELLIDOS = "apellidos"
        private const val COL_PRF_EMAIL = "email"
        private const val COL_PRF_XP = "xp_total"
        private const val COL_PRF_MONEDAS = "monedas"
        private const val COL_PRF_RACHA = "racha_actual"
        private const val COL_PRF_RACHA_MAX = "racha_maxima"
        private const val COL_PRF_NIVEL = "nivel_usuario"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES (
                $COL_CAT_ID INTEGER PRIMARY KEY,
                $COL_CAT_NOMBRE TEXT NOT NULL,
                $COL_CAT_DESCRIPCION TEXT,
                $COL_CAT_COUNT INTEGER DEFAULT 0
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_COURSES (
                $COL_CRS_ID INTEGER PRIMARY KEY,
                $COL_CRS_CAT_ID INTEGER NOT NULL,
                $COL_CRS_NOMBRE TEXT NOT NULL,
                $COL_CRS_DESCRIPCION TEXT,
                $COL_CRS_ICONO TEXT,
                $COL_CRS_XP_REQ INTEGER DEFAULT 0,
                $COL_CRS_XP_REC INTEGER DEFAULT 0,
                $COL_CRS_ACTIVO INTEGER DEFAULT 1
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_PROFILE (
                $COL_PRF_ID INTEGER PRIMARY KEY,
                $COL_PRF_NOMBRES TEXT,
                $COL_PRF_APELLIDOS TEXT,
                $COL_PRF_EMAIL TEXT,
                $COL_PRF_XP INTEGER DEFAULT 0,
                $COL_PRF_MONEDAS INTEGER DEFAULT 0,
                $COL_PRF_RACHA INTEGER DEFAULT 0,
                $COL_PRF_RACHA_MAX INTEGER DEFAULT 0,
                $COL_PRF_NIVEL INTEGER DEFAULT 1
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        onCreate(db)
    }

    // ============================================================
    // PERSISTENCIA: CATEGORÍAS
    // ============================================================

    fun saveCategories(categories: List<CourseCategory>) {
        if (categories.isEmpty()) return
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (cat in categories) {
                val cv = ContentValues().apply {
                    put(COL_CAT_ID, cat.idCategory)
                    put(COL_CAT_NOMBRE, cat.title)
                    put(COL_CAT_DESCRIPCION, cat.description)
                    put(COL_CAT_COUNT, cat.coursesCount)
                }
                db.insertWithOnConflict(TABLE_CATEGORIES, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar categorias en SQLite", e)
        } finally {
            db.endTransaction()
        }
    }

    fun getCategories(): List<CourseCategory> {
        val list = mutableListOf<CourseCategory>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, "$COL_CAT_NOMBRE ASC")
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CAT_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_NOMBRE))
                    val desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_DESCRIPCION)) ?: ""
                    val count = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAT_COUNT))
                    list.add(CourseCategory(idCategory = id, title = title, description = desc, coursesCount = count))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al leer categorias desde SQLite", e)
        } finally {
            cursor?.close()
        }
        return list
    }

    // ============================================================
    // PERSISTENCIA: CURSOS (HOME & CATEGORIAS)
    // ============================================================

    fun saveCourses(courses: List<Course>) {
        if (courses.isEmpty()) return
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (course in courses) {
                val cv = ContentValues().apply {
                    put(COL_CRS_ID, course.idCurso)
                    put(COL_CRS_CAT_ID, course.idCategoria)
                    put(COL_CRS_NOMBRE, course.nombre)
                    put(COL_CRS_DESCRIPCION, course.descripcion)
                    put(COL_CRS_ICONO, course.icono)
                    put(COL_CRS_XP_REQ, course.xpRequerido)
                    put(COL_CRS_XP_REC, course.xpRecompensa)
                    put(COL_CRS_ACTIVO, if (course.activo) 1 else 0)
                }
                db.insertWithOnConflict(TABLE_COURSES, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar cursos en SQLite", e)
        } finally {
            db.endTransaction()
        }
    }

    fun getCourses(): List<Course> {
        val list = mutableListOf<Course>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.query(TABLE_COURSES, null, null, null, null, null, "$COL_CRS_NOMBRE ASC")
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CRS_ID))
                    val catId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CRS_CAT_ID))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_CRS_NOMBRE))
                    val desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_CRS_DESCRIPCION)) ?: ""
                    val icono = cursor.getString(cursor.getColumnIndexOrThrow(COL_CRS_ICONO))
                    val xpReq = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CRS_XP_REQ))
                    val xpRec = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CRS_XP_REC))
                    val activo = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CRS_ACTIVO)) == 1

                    list.add(
                        Course(
                            idCurso = id,
                            idCategoria = catId,
                            nombre = nombre,
                            descripcion = desc,
                            icono = icono,
                            xpRequerido = xpReq,
                            xpRecompensa = xpRec,
                            activo = activo,
                            fechaCreacion = null,
                            fechaActualizacion = null
                        )
                    )
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al leer cursos desde SQLite", e)
        } finally {
            cursor?.close()
        }
        return list
    }

    // ============================================================
    // PERSISTENCIA: PERFIL DE USUARIO (HOME)
    // ============================================================

    fun saveProfile(profile: UserProfile) {
        val db = writableDatabase
        try {
            val cv = ContentValues().apply {
                put(COL_PRF_ID, profile.idUsuario)
                put(COL_PRF_NOMBRES, profile.nombres)
                put(COL_PRF_APELLIDOS, profile.apellidos)
                put(COL_PRF_EMAIL, profile.email)
                put(COL_PRF_XP, profile.xpTotal)
                put(COL_PRF_MONEDAS, profile.monedas)
                put(COL_PRF_RACHA, profile.rachaActual)
                put(COL_PRF_RACHA_MAX, profile.rachaMaxima)
                put(COL_PRF_NIVEL, profile.nivelUsuario)
            }
            db.insertWithOnConflict(TABLE_PROFILE, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar perfil en SQLite", e)
        }
    }

    fun getProfile(): UserProfile? {
        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.query(TABLE_PROFILE, null, null, null, null, null, null, "1")
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_PRF_ID))
                val nombres = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRF_NOMBRES)) ?: ""
                val apellidos = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRF_APELLIDOS)) ?: ""
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRF_EMAIL)) ?: ""
                val xp = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRF_XP))
                val monedas = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRF_MONEDAS))
                val racha = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRF_RACHA))
                val rachaMax = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRF_RACHA_MAX))
                val nivel = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRF_NIVEL))

                return UserProfile(
                    idUsuario = id,
                    idRol = null,
                    nombreRol = null,
                    idEstadoUsuario = null,
                    nombreEstado = null,
                    nombres = nombres,
                    apellidos = apellidos,
                    email = email,
                    emailVerificado = true,
                    fechaNacimiento = null,
                    avatarUrl = null,
                    biografia = null,
                    telefono = null,
                    pais = null,
                    ciudad = null,
                    xpTotal = xp,
                    monedas = monedas,
                    nivelUsuario = nivel,
                    rachaActual = racha,
                    rachaMaxima = rachaMax,
                    fechaRegistro = null
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al leer perfil desde SQLite", e)
        } finally {
            cursor?.close()
        }
        return null
    }
}
