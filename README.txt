>Backend
    >data 
        todos los archivos
    >src/spring
        >config
            conexion backend-frontend
        >model 
            aqui se definen las entidades, sus getters y setters
            aspectos importantes: perfil es una super clase de la que heredan administrador y usuario.
            ademas, anuncio hereda de publicacion. LoginRequest es una clase dedicada al login.
        >controller (recibe pedidos del frontend a traves de las API)
            >AnuncioControladores
                >CrearAnuncio.java
                    API: /api/anuncio/crear de tipo Post. 
                >VerAnuncio.java
                    API: 
                        /api/anuncio de tipo GET. Devuelve el anuncio activo actual. 
                        /api/anuncio/{id} de tipo GET. Devuelve el anuncio correspondiente al id dado, si existe; si no, retorna un 404.
            >chatControladores
                >crearChat.java
                    API:/api/chat/crear de tipo POST
                >enviarMensaje.java
                    API: /api/mensaje/enviar de tipo Post
                >VerChats.java
                    ...
                    ...
                    ...
        >service aqui se encuentra la logica de negocio, aqui ocurren las validaciones, es el
        puente entre controller y repository. lo usamos para separar la logica de las peticiones HTTP
        y la persistencia de los datos.
            >puedes asociar cada controlador con su service en la mayoria de los casos.
        >repository: logica de persistencia de datos. cada service tiene un repository
        define que hace.
            >implementation
                aqui se dice como se hace.
        SpringApp.java. app principal, es el servidor. boton de encendido de la app


        
                    

        