 * FUNCIONAMIENTO EN EL PATRÓN MVC (Model-View-Controller) / SERVICE TO WORKER:
 * 
 * 1. El AppController (Front Controller) recibe una petición HTTP.
 * 
 * 2. Determina qué acción específica se ha solicitado (ej., "agregarCD", "verCarrito").
 * 3. Crea o localiza una instancia de una clase que implementa esta interfaz Accion
 *    (por ejemplo, AccionAgregarAlCarrito, AccionVerCarrito). Estas clases
 *    son los "Workers" o "Helpers" que contienen la lógica de negocio.
 * 
 * 4. El AppController invoca el método ejecutar() de la instancia de Accion seleccionada.
 * 
 * 5. La clase Accion concreta:
 *    a. Realiza la lógica de negocio (interactúa con el Modelo: JavaBeans, DAOs).
 * 
 *    b. Prepara los datos que la Vista (JSP) necesitará (generalmente poniéndolos
 *       como atributos en el objeto request).
 * 
 *    c. Decide a qué Vista (JSP) se debe redirigir o hacer forward.
 *
 * VENTAJA DE USAR UNA INTERFAZ:
 * - El AppController solo necesita conocer la interfaz Accion,
 *   no los detalles de implementación de cada acción específica. Esto hace que el
 *   AppController sea más genérico y fácil de mantener.
 * 
 * - Para añadir una nueva funcionalidad a la aplicación, solo
 *   necesitas crear una nueva clase que implemente esta interfaz Accion y
 *   añadir un caso al AppController para que la invoque. No necesitas modificar
 *   la estructura central del controlador para cada nueva acción.
 * 
 * - El AppController puede tratar a todas las diferentes clases de acción
 *   de manera uniforme a través de la referencia de tipo Accion.
 *
 * Cada implementación de esta interfaz:
 * 
 *  - Manejará la lógica de negocio específica para una solicitud particular.
 * 
 *  - Preparará los datos necesarios para la vista (si es que hay una vista a la que hacer forward).
 * 
 *  - Determinará la ruta a la siguiente vista (JSP) a la que se debe despachar la petición,
 *    o manejará la respuesta completamente (por ejemplo, realizando un sendRedirect).