/* ============================================================
   JAGUAR SHOP UAM — interacciones de front-end
   Carrito (localStorage), drawer, filtros de catálogo,
   selector de talla/cantidad y menú móvil.
   ============================================================ */
(function () {
  "use strict";

  var KEY = "jaguarCart";
  var fmt = function (n) { return "C$" + Math.round(n).toLocaleString("es-NI"); };

  function read() { try { return JSON.parse(localStorage.getItem(KEY)) || []; } catch (e) { return []; } }
  function write(c) { localStorage.setItem(KEY, JSON.stringify(c)); }

  function add(item) {
    var cart = read();
    var i = cart.findIndex(function (x) { return x.id === item.id && x.size === item.size; });
    if (i >= 0) cart[i].qty += item.qty; else cart.push(item);
    write(cart);
    render();
    open();
  }
  function changeQty(idx, d) {
    var cart = read();
    cart[idx].qty += d;
    if (cart[idx].qty <= 0) cart.splice(idx, 1);
    write(cart); render();
  }
  function remove(idx) { var cart = read(); cart.splice(idx, 1); write(cart); render(); }

  function open() { var d = document.getElementById("jsCart"); if (d) d.classList.add("is-open"); }
  function close() { var d = document.getElementById("jsCart"); if (d) d.classList.remove("is-open"); }

  function render() {
    var cart = read();
    var count = cart.reduce(function (a, c) { return a + c.qty; }, 0);
    var total = cart.reduce(function (a, c) { return a + c.precio * c.qty; }, 0);

    // badge
    document.querySelectorAll("[data-cart-count]").forEach(function (b) {
      b.textContent = count;
      b.style.display = count > 0 ? "flex" : "none";
    });

    var body = document.getElementById("jsCartBody");
    var empty = document.getElementById("jsCartEmpty");
    var foot = document.getElementById("jsCartFoot");
    if (!body) return;

    if (count === 0) {
      if (empty) empty.style.display = "flex";
      body.style.display = "none";
      if (foot) foot.style.display = "none";
      return;
    }
    if (empty) empty.style.display = "none";
    body.style.display = "flex";
    if (foot) foot.style.display = "block";

    body.innerHTML = cart.map(function (it, idx) {
      var img = it.imagen
        ? '<img src="/uploads/productos/' + it.imagen + '" alt="">'
        : "";
      return '' +
        '<div class="js-cart__item">' +
          '<div class="js-cart__thumb">' + img + '</div>' +
          '<div style="flex:1;min-width:0;">' +
            '<div class="js-cart__iname">' + it.nombre + '</div>' +
            '<div class="js-cart__imeta">' + (it.size && it.size !== "Única" ? "Talla " + it.size + " · " : "") + fmt(it.precio) + '</div>' +
            '<div style="display:flex;align-items:center;justify-content:space-between;">' +
              '<div class="js-cart__qty">' +
                '<button data-dec="' + idx + '">−</button>' +
                '<span>' + it.qty + '</span>' +
                '<button data-inc="' + idx + '">+</button>' +
              '</div>' +
              '<span class="js-cart__isub">' + fmt(it.precio * it.qty) + '</span>' +
            '</div>' +
          '</div>' +
          '<button class="js-cart__rm" data-rm="' + idx + '" title="Quitar">' +
            '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="m6 6 12 12M18 6 6 18"/></svg>' +
          '</button>' +
        '</div>';
    }).join("");

    var tot = document.getElementById("jsCartTotal");
    if (tot) tot.textContent = fmt(total);
  }

  document.addEventListener("click", function (e) {
    var t = e.target.closest("[data-add]");
    if (t) {
      e.preventDefault();
      add({
        id: t.getAttribute("data-id"),
        nombre: t.getAttribute("data-nombre"),
        precio: parseFloat(t.getAttribute("data-precio")) || 0,
        imagen: t.getAttribute("data-imagen") || "",
        size: t.getAttribute("data-size") || "Única",
        qty: parseInt(t.getAttribute("data-qty") || "1", 10)
      });
      return;
    }
    if (e.target.closest("[data-cart-open]")) { open(); return; }
    if (e.target.closest("[data-cart-close]")) { close(); return; }

    var inc = e.target.closest("[data-inc]"); if (inc) { changeQty(+inc.getAttribute("data-inc"), 1); return; }
    var dec = e.target.closest("[data-dec]"); if (dec) { changeQty(+dec.getAttribute("data-dec"), -1); return; }
    var rm = e.target.closest("[data-rm]"); if (rm) { remove(+rm.getAttribute("data-rm")); return; }

    // chips de categoría
    var chip = e.target.closest("[data-filter]");
    if (chip) {
      var val = chip.getAttribute("data-filter");
      document.querySelectorAll("[data-filter]").forEach(function (c) { c.classList.toggle("is-active", c === chip); });
      document.querySelectorAll("[data-categoria]").forEach(function (card) {
        var show = val === "*" || card.getAttribute("data-categoria") === val;
        card.style.display = show ? "" : "none";
      });
      return;
    }

    // menú móvil
    if (e.target.closest("[data-nav-toggle]")) {
      var links = document.querySelector(".js-navlinks");
      if (links) links.classList.toggle("is-open");
      return;
    }
  });

  // Detalle: selector de talla + cantidad
  document.addEventListener("DOMContentLoaded", function () {
    render();

    var sizes = document.querySelectorAll("[data-size-opt]");
    var addBtn = document.querySelector("[data-detail-add]");
    sizes.forEach(function (b) {
      b.addEventListener("click", function () {
        sizes.forEach(function (x) { x.classList.remove("is-active"); });
        b.classList.add("is-active");
        if (addBtn) addBtn.setAttribute("data-size", b.getAttribute("data-size-opt"));
      });
    });

    var qtyEl = document.getElementById("jsQty");
    var minus = document.querySelector("[data-qty-minus]");
    var plus = document.querySelector("[data-qty-plus]");
    function setQty(v) {
      v = Math.max(1, v);
      if (qtyEl) qtyEl.textContent = v;
      if (addBtn) addBtn.setAttribute("data-qty", v);
    }
    if (minus) minus.addEventListener("click", function () { setQty((+qtyEl.textContent) - 1); });
    if (plus) plus.addEventListener("click", function () { setQty((+qtyEl.textContent) + 1); });

    // ESC cierra el carrito
    document.addEventListener("keydown", function (e) { if (e.key === "Escape") close(); });

    // Finalizar compra -> envia el carrito al backend (POST /pedidos/checkout)
    var checkoutBtn = document.querySelector("[data-checkout]");
    if (checkoutBtn) {
      checkoutBtn.addEventListener("click", function () {
        var cart = read();
        if (!cart.length) return;

        var headers = { "Content-Type": "application/json" };
        var tokenMeta = document.querySelector('meta[name="_csrf"]');
        var headerMeta = document.querySelector('meta[name="_csrf_header"]');
        if (tokenMeta && headerMeta && headerMeta.getAttribute("content")) {
          headers[headerMeta.getAttribute("content")] = tokenMeta.getAttribute("content");
        }

        checkoutBtn.disabled = true;
        var original = checkoutBtn.textContent;
        checkoutBtn.textContent = "Procesando…";

        fetch("/pedidos/checkout", {
          method: "POST",
          headers: headers,
          body: JSON.stringify({
            items: cart.map(function (x) { return { id: x.id, qty: x.qty }; })
          })
        })
          .then(function (r) {
            // Sin sesión, Spring redirige al login: mandamos al usuario ahí.
            if (r.redirected || r.status === 401 || r.status === 403) {
              window.location.href = "/login";
              return null;
            }
            return r.json().catch(function () { return {}; });
          })
          .then(function (d) {
            if (!d) return;
            if (d.ok) {
              localStorage.removeItem(KEY);
              window.location.href = "/pedidos/" + d.pedidoId;
            } else {
              alert(d.error ? d.error : "No se pudo completar la compra.");
              checkoutBtn.disabled = false;
              checkoutBtn.textContent = original;
            }
          })
          .catch(function () {
            alert("Error de conexión al finalizar la compra.");
            checkoutBtn.disabled = false;
            checkoutBtn.textContent = original;
          });
      });
    }

    // Filtros desde la URL en el catálogo: ?q=texto  y/o  ?cat=Categoría
    (function () {
      var grid = document.querySelector(".js-grid");
      if (!grid) return;
      var params = new URLSearchParams(window.location.search);
      var q = (params.get("q") || "").trim().toLowerCase();
      var cat = params.get("cat");
      if (!q && !cat) return;

      // Activa el chip de la categoría si corresponde
      if (cat) {
        document.querySelectorAll("[data-filter]").forEach(function (c) {
          c.classList.toggle("is-active", c.getAttribute("data-filter") === cat);
        });
      }

      grid.querySelectorAll(".js-card").forEach(function (card) {
        var okCat = !cat || card.getAttribute("data-categoria") === cat;
        var nameEl = card.querySelector(".js-card__name");
        var name = nameEl ? nameEl.textContent.toLowerCase() : "";
        var okQ = !q || name.indexOf(q) >= 0;
        card.style.display = (okCat && okQ) ? "" : "none";
      });
    })();
  });
})();
