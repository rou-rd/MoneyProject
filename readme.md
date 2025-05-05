Sure, you can change the ports for pgAdmin to something else. For example, you can use port `5050` for pgAdmin. Here's how you can set it up:

### Step-by-Step Guide

1. **Modify `docker-compose.yml` File:**

   Update the `docker-compose.yml` file to change the pgAdmin port to `5050` and include both PostgreSQL and pgAdmin services.

   ```yaml
   version: '3.8'
   services:
     postgres:
       image: postgres:13
       container_name: postgres
       environment:
         POSTGRES_DB: mydatabase
         POSTGRES_USER: myuser
         POSTGRES_PASSWORD: mypassword
       ports:
         - "5432:5432"
       volumes:
         - postgres-data:/var/lib/postgresql/data

     pgadmin:
       image: dpage/pgadmin4
       container_name: pgadmin
       environment:
         PGADMIN_DEFAULT_EMAIL: admin@example.com
         PGADMIN_DEFAULT_PASSWORD: admin
       ports:
         - "5050:80"  # Change this line to use port 5050
       depends_on:
         - postgres

   volumes:
     postgres-data:
   ```

   Here, we have mapped the internal port `80` of the pgAdmin container to the external port `5050`.

2. **Start the Containers:**

   Run the following command to start both the PostgreSQL and pgAdmin containers:

   ```sh
   docker-compose up
   ```

3. **Access pgAdmin:**

   - Open your web browser and go to `http://localhost:5050`.
   - Log in with the credentials you provided in the `docker-compose.yml` file (e.g., `admin@example.com` / `admin`).

4. **Connect pgAdmin to Your PostgreSQL Database:**

   - After logging in, create a new server connection in pgAdmin:
     - Click on "Add New Server".
     - In the "Create - Server" dialog, provide the following details:

       - **General Tab:**
         - Name: Any name for your connection (e.g., "Local PostgreSQL").

       - **Connection Tab:**
         - Hostname/Address: `postgres` (This is the name of the PostgreSQL service defined in the `docker-compose.yml` file)
         - Port: `5432`
         - Maintenance Database: `mydatabase`
         - Username: `myuser`
         - Password: `mypassword`

     - Click "Save" to create the connection.

5. **Configure Spring Boot to Connect to PostgreSQL:**

   - Ensure your Spring Boot application is configured to connect to the PostgreSQL database. Update your `application.properties` or `application.yml`:

     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
     spring.datasource.username=myuser
     spring.datasource.password=mypassword
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
     ```

     or in `application.yml`:

     ```yaml
     spring:
       datasource:
         url: jdbc:postgresql://localhost:5432/mydatabase
         username: myuser
         password: mypassword
       jpa:
         properties:
           hibernate:
             dialect: org.hibernate.dialect.PostgreSQLDialect
     ```

6. **Run Your Spring Boot Application:**

   - Start your Spring Boot application, and it should connect to the PostgreSQL database running in the Docker container.

### Summary

By following these steps, you can change the port for pgAdmin to `5050`, set up both PostgreSQL and pgAdmin using Docker, and ensure that your Spring Boot application can connect to the PostgreSQL database.