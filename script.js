document.addEventListener("DOMContentLoaded", iniciarAplicacion);


let listaTarjetas = [];
// Esta función se ejecuta cuando el contenido del DOM ha sido completamente cargado. 
// Aquí se inicializan los eventos y se carga el archivo XML con la información de las fotografías 
// para crear las tarjetas en la galería.
function iniciarAplicacion(){
    cargarXML();

    document
    .getElementById("seleccionTemaVisual")
    .addEventListener("change", cambiarTema);
    cambiarTema();

    document
    .getElementById("accionEjecutarBusqueda")
    .addEventListener("click", ejecutarBusqueda);

    document
    .getElementById("activarFormularioNuevaTarjeta")
    .addEventListener("click", abrirModalTarjeta);

    document
    .getElementById("cerrarVentanaTarjeta")
    .addEventListener("click", cerrarModalTarjeta);

    document
    .getElementById("cerrarVentanaTema")
    .addEventListener("click", cerrarModalTema);

    document
    .getElementById("confirmarAplicacionTema")
    .addEventListener("click", aplicarTemaPersonalizado);

    document
    .getElementById("formularioIngresoTarjeta")
    .addEventListener("submit", crearNuevaTarjeta);
}

// Esta función se encarga de cargar el archivo XML que contiene la información de las fotografías. 
// Utiliza la API Fetch para obtener el archivo, luego lo parsea y extrae los datos necesarios para crear 
// las tarjetas en la galería.
function cargarXML(){
    fetch("data/data.xml")
    .then(respuesta => respuesta.text())
    .then(datos => {
        let parser = new DOMParser();
        let xml = parser.parseFromString(datos,"text/xml");
        let fotografias = xml.getElementsByTagName("fotografia");

        for(let i = 0; i < fotografias.length; i++){
            let titulo = fotografias[i].getElementsByTagName("titulo")[0].textContent;
            let autor = fotografias[i].getElementsByTagName("autor")[0].textContent;
            let categoria = fotografias[i].getElementsByTagName("categoria")[0].textContent;
            let descripcion = fotografias[i].getElementsByTagName("descripcion")[0].textContent;
            let imagenNodo = fotografias[i].getElementsByTagName("img")[0];
            let imagen = imagenNodo ? imagenNodo.textContent.trim() : "";

            crearTarjetaDOM(titulo, autor, categoria, descripcion, imagen);
        }
    })
    .catch(error => console.error("Error cargando XML:", error));
}

// Esta función crea una tarjeta en el DOM para cada fotografía utilizando los datos extraídos del archivo XML. 
// Cada tarjeta incluye una imagen, un título y un evento de clic que abre un modal con los detalles de la fotografía.
function crearTarjetaDOM(titulo, autor, categoria, descripcion, imagen){
    let contenedor = document.getElementById("contenedorPrincipalGaleria");

    let tarjeta = document.createElement("div");
    tarjeta.className = "tarjetaElementoGaleria";

    let imagenElemento = document.createElement("img");
    imagenElemento.src = imagen;
    imagenElemento.alt = titulo;
    imagenElemento.onerror = function() { this.src = "img/default.png"; }

    let contenido = document.createElement("div");
    contenido.className = "contenidoTarjetaInterno";

    let tituloElemento = document.createElement("h3");
    tituloElemento.textContent = titulo;

    contenido.appendChild(tituloElemento);
    tarjeta.appendChild(imagenElemento);
    tarjeta.appendChild(contenido);
    contenedor.appendChild(tarjeta);

    tarjeta.addEventListener("click", () => abrirModalDetalle(titulo, autor, categoria, descripcion, imagen));
    listaTarjetas.push(tarjeta);
}

// Esta función se encarga de abrir un modal que muestra los detalles de la fotografía cuando se hace clic en una tarjeta. 
// El modal incluye la imagen, el título, el autor, la categoría y la descripción de la fotografía. 
// También se configuran eventos para cerrar el modal al hacer clic en el botón de cerrar o fuera del contenido del modal.
function abrirModalDetalle(titulo, autor, categoria, descripcion, imagen){
    let modal = document.getElementById("modalDetalleTarjeta");
    document.getElementById("modalImagen").src = imagen;
    document.getElementById("modalTitulo").textContent = titulo;
    document.getElementById("modalAutor").textContent = "Autor: " + autor;
    document.getElementById("modalCategoria").textContent = "Categoría: " + categoria;
    document.getElementById("modalDescripcion").textContent = descripcion;
    modal.style.display = "flex";

    document.getElementById("cerrarModalDetalle").onclick = () => { modal.style.display = "none"; }
    window.onclick = (event) => { if(event.target == modal) modal.style.display = "none"; }
}

// Esta función se encarga de ejecutar la búsqueda de tarjetas en la galería. 
// Toma el texto ingresado en el campo de búsqueda, lo convierte a minúsculas y luego recorre la lista de tarjetas para mostrar u ocultar cada tarjeta según si su título incluye el texto de búsqueda o no.
function ejecutarBusqueda(){
    let texto = document.getElementById("campoTextoBusquedaGeneral").value.toLowerCase();

    listaTarjetas.forEach(tarjeta => {
        let titulo = tarjeta.querySelector("h3").textContent.toLowerCase();
        tarjeta.style.display = titulo.includes(texto) ? "flex" : "none";
    });
}

// Esta función se encarga de cambiar el tema visual de la página según la opción seleccionada en el menú desplegable. 
// Dependiendo del valor seleccionado (oscuro, claro o personalizado), se aplican diferentes estilos a los elementos de la página. 
// Si se selecciona "personalizado", se muestra un modal para que el usuario pueda ingresar sus propios colores para el encabezado, la zona central y el pie de página.
function cambiarTema(){
    let valor = document.getElementById("seleccionTemaVisual").value;

    if(valor==="oscuro"){
        document.body.style.background="#0e0202ff";
        document.querySelector("header").style.background="rgba(51, 44, 44, 1)";
        document.querySelector("main").style.background="#3b3232ff";
        document.querySelector("footer").style.background="rgba(8, 5, 5, 1)";
        document.body.style.color="#000000ff";
    } else if(valor==="claro"){
        document.body.style.background="#f1e7e7ff";
        document.querySelector("header").style.background="#b8b8b8ff";
        document.querySelector("main").style.background="#ffffffff";
        document.querySelector("footer").style.background="#888080ff";
        document.body.style.color="#0f0202ff";
    } else if(valor==="personalizado"){
        document.getElementById("ventanaFlotanteConfiguracionTema").style.display="flex";
    }
}

// Esta función se encarga de aplicar el tema personalizado ingresado por el usuario. 
// Toma los valores de color ingresados para el encabezado, la zona central y el pie de página, y luego aplica esos colores a los elementos correspondientes de la página. 
// Después de aplicar el tema personalizado, se cierra el modal de configuración del tema.
function aplicarTemaPersonalizado(){
    let colorHeader = document.getElementById("entradaColorEncabezado").value;
    let colorMain = document.getElementById("entradaColorZonaCentral").value;
    let colorFooter = document.getElementById("entradaColorPiePagina").value;
    document.querySelector("header").style.background=colorHeader;
    document.querySelector("main").style.background=colorMain;
    document.querySelector("footer").style.background=colorFooter;
    cerrarModalTema();
}

// Esta función se encarga de abrir el modal para crear una nueva tarjeta. 
// Cuando se hace clic en el botón para agregar una nueva tarjeta, se muestra un formulario donde el usuario puede ingresar el título, la descripción y seleccionar una imagen para la nueva tarjeta. 
// El modal se muestra estableciendo su propiedad de estilo "display" a "flex".
function abrirModalTarjeta(){
    document.getElementById("ventanaFlotanteNuevaTarjeta").style.display="flex";
}

// Esta función se encarga de cerrar el modal para crear una nueva tarjeta. 
// Cuando se hace clic en el botón de cerrar dentro del modal, se oculta el formulario estableciendo su propiedad de estilo "display" a "none".
function cerrarModalTarjeta(){
    document.getElementById("ventanaFlotanteNuevaTarjeta").style.display="none";
}

// Esta función se encarga de cerrar el modal de configuración del tema. 
// Cuando se hace clic en el botón de cerrar dentro del modal, se oculta el formulario estableciendo su propiedad de estilo "display" a "none".
function cerrarModalTema(){
    document.getElementById("ventanaFlotanteConfiguracionTema").style.display="none";
}

// Esta función se encarga de crear una nueva tarjeta en la galería utilizando los datos ingresados por el usuario en el formulario del modal. 
// Toma el título, la descripción y la imagen seleccionada por el usuario, y luego utiliza un FileReader para leer la imagen como una URL de datos en formato base64. 
// Una vez que se obtiene la URL de datos de la imagen, se llama a la función "crearTarjetaDOM" para crear la nueva tarjeta en el DOM con los datos ingresados por el usuario. 
// Después de crear la tarjeta, se cierra el modal de creación de tarjetas.
function crearNuevaTarjeta(evento){
    evento.preventDefault();

    let titulo = document.getElementById("entradaTituloTarjeta").value;
    let descripcion = document.getElementById("entradaTextoDescripcion").value;
    let autor = "Usuario";
    let categoria = "Personal";

    let archivo = document.getElementById("selectorArchivoImagen").files[0];
    if(!archivo) return;

    let reader = new FileReader();
    reader.onload = function(elemento){
        let imagenBase64 = elemento.target.result;
        crearTarjetaDOM(titulo, autor, categoria, descripcion, imagenBase64);
    };
    reader.readAsDataURL(archivo);

    cerrarModalTarjeta();
}