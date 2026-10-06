#!/bin/bash
set -e
echo "Setting up TechMentor database..."
psql -U postgres -c "DROP DATABASE IF EXISTS techmentor;"
psql -U postgres -c "CREATE DATABASE techmentor;"
psql -U postgres -c "ALTER USER postgres WITH PASSWORD 'postgres';"
psql -U postgres -d techmentor < /home/kaos/Proyectos/techmentor/database/01_schema.sql
psql -U postgres -d techmentor < /home/kaos/Proyectos/techmentor/database/02_data.sql
echo "Database techmentor initialized successfully!"
