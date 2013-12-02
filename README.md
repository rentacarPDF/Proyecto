RentacarPDF
========

Proyecto realizado para la carrera de Analista de Sistemas de Información dictada en el Instituto de Formación y Educación Superior (IFES) de la ciudad de Neuquén (Argentina), para la materia Proyecto Final durante el ciclo lectivo 2013, con el fin de aplicar los conocimientos aprendidos durante la carrera.

Como marco de trabajo para la gestión y el desarrollo del software, como así también para optimizar el trabajo en equipo se optó por utilizar la metodología ágil SCRUM. La cual se basa en realizar entregas parciales y regulares de los avances del proyecto de cada integrante del grupo.

El diseño y modelado del proyecto se realizó a través de DDD (Domain-Driven Design).
El lenguaje de programación que utilizamos es Java, como herramienta de gestión y construcción del proyecto se utiliza Maven con el fin de controlar las dependencias, componentes externos y el orden de construcción del proyecto. Además para controlar las versiones del código fuente se utilizó la herramienta de control de versiones GIT. 

El framework utilizado es [Apache Isis](http://isis/apache.org)' y para almacenar los datos se utilizó Postgres-Sql.

Además de contar con un repositorio online en GitHub para el manejo del proyecto en forma distribuida.

Dentro del sistema de gestión, se puede administrar:

* Vehículos 
* Categorías
* Clientes 
* Alquileres 
* Adicionales
* Estadísticas con gráficos
* Historiales
 
Además va a contar con los siguientes servicios:

* Posibilidad de Notificaciones de los alquileres realizadas vía correo electrónico utilizando la API JavaMail.
* Recepcion de emails utilizando la API JavaMail.
* Posibilidad de enviar tweets a la plataforma de Twitter utilizando la libreria de Twitter4J.
* Descargar diferentes listados en formato .xls (Apache POI).
* Descargar gráficos de Estadísticas de Alquileres en formatos pdf o jpg utilizando la libreria JavaScript WickedCharts.
* Visualizaciones en Calendarios utilizando el componente Wicket-full-Calendar.
