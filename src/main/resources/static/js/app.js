const estado = document.getElementById("estado-catalogo");
const grilla = document.getElementById("grilla-motos");
const modal = document.getElementById("modal-moto");

let whatsapp = "595981000000";
let filtroActual = "todas";

const gs = new Intl.NumberFormat("es-PY", {
  style: "currency",
  currency: "PYG",
  maximumFractionDigits: 0
});

function enlaceWhatsApp(texto) {
  return `https://wa.me/${whatsapp}?text=${encodeURIComponent(texto)}`;
}

function mensajeMoto(moto) {
  return `Hola Chacomer, quiero consultar la ${moto.marca} ${moto.modelo} (${moto.cilindrada} cc). Vi el precio en Gs. ${Number(moto.precio).toLocaleString("es-PY")}. ¿Sigue disponible?`;
}

function mensajeGeneral() {
  return "Hola Chacomer, quiero información sobre las motos de media y alta cilindrada.";
}

async function cargarConfig() {
  const res = await fetch("/api/config");
  const data = await res.json();
  whatsapp = data.whatsapp;
  const header = document.getElementById("btn-whatsapp-header");
  const contacto = document.getElementById("btn-whatsapp-contacto");
  header.href = enlaceWhatsApp(mensajeGeneral());
  contacto.href = enlaceWhatsApp(mensajeGeneral());
}

async function cargarMotos(filtro) {
  filtroActual = filtro;
  document.querySelectorAll(".filtro").forEach((btn) => {
    btn.classList.toggle("activo", btn.dataset.filtro === filtro);
  });
  estado.textContent = "Cargando motos…";
  const url = filtro === "todas" ? "/api/motos" : `/api/motos?categoria=${filtro}`;
  const res = await fetch(url);
  if (!res.ok) {
    estado.textContent = "No se pudo leer el catálogo. ¿Está corriendo el backend y PostgreSQL?";
    return;
  }
  const motos = await res.json();
  estado.textContent = motos.length ? `${motos.length} moto${motos.length === 1 ? "" : "s"} en vitrina` : "No hay motos en esta categoría.";
  grilla.innerHTML = motos.map((moto) => `
    <article class="tarjeta">
      <img src="${moto.imagenUrl}" alt="${moto.marca} ${moto.modelo}" loading="lazy">
      <div class="tarjeta-cuerpo">
        <span class="badge">${moto.categoria.nombre} · ${moto.cilindrada} cc</span>
        <h3>${moto.marca} ${moto.modelo}</h3>
        <p class="precio">${gs.format(moto.precio)}</p>
        <button type="button" class="btn btn-primario" data-id="${moto.id}">Ver y consultar</button>
      </div>
    </article>
  `).join("");
}

function abrirModal(moto) {
  document.getElementById("modal-img").src = moto.imagenUrl;
  document.getElementById("modal-img").alt = `${moto.marca} ${moto.modelo}`;
  document.getElementById("modal-cat").textContent = moto.categoria.nombre;
  document.getElementById("modal-titulo").textContent = `${moto.marca} ${moto.modelo}`;
  document.getElementById("modal-desc").textContent = moto.descripcion;
  document.getElementById("modal-cc").textContent = `${moto.cilindrada} cc`;
  document.getElementById("modal-color").textContent = moto.color;
  document.getElementById("modal-precio").textContent = gs.format(moto.precio);
  document.getElementById("modal-wa").href = enlaceWhatsApp(mensajeMoto(moto));
  modal.showModal();
}

grilla.addEventListener("click", async (evento) => {
  const boton = evento.target.closest("[data-id]");
  if (!boton) return;
  const res = await fetch(`/api/motos/${boton.dataset.id}`);
  if (res.ok) abrirModal(await res.json());
});

document.querySelectorAll("[data-filtro]").forEach((btn) => {
  btn.addEventListener("click", () => cargarMotos(btn.dataset.filtro));
});

document.getElementById("modal-cerrar").addEventListener("click", () => modal.close());
modal.addEventListener("click", (evento) => {
  if (evento.target === modal) modal.close();
});

cargarConfig()
  .then(() => cargarMotos("todas"))
  .catch(() => {
    estado.textContent = "Error al conectar con la API. Revisá que Spring Boot esté en el puerto 8080.";
  });
