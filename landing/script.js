(function () {
  const reduzirMovimento = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  const topo = document.getElementById("topo");
  const atualizarTopo = () => topo.classList.toggle("topo--rolado", window.scrollY > 12);
  atualizarTopo();
  window.addEventListener("scroll", atualizarTopo, { passive: true });

  const menuBotao = document.getElementById("menuBotao");
  const menu = document.getElementById("menuLinks");
  const fecharMenu = () => {
    menu.classList.remove("menu--aberto");
    menuBotao.setAttribute("aria-expanded", "false");
    menuBotao.setAttribute("aria-label", "Abrir menu");
  };
  menuBotao.addEventListener("click", () => {
    const aberto = menu.classList.toggle("menu--aberto");
    menuBotao.setAttribute("aria-expanded", String(aberto));
    menuBotao.setAttribute("aria-label", aberto ? "Fechar menu" : "Abrir menu");
  });
  menu.querySelectorAll("a").forEach((link) => link.addEventListener("click", fecharMenu));
  document.addEventListener("keydown", (evento) => {
    if (evento.key === "Escape") fecharMenu();
  });

  const elementos = document.querySelectorAll(".revelar");
  document.querySelectorAll(".cartoes, .entidades, .equipe, .tecnologias, .ods, .objetivos, .perfis, .numeros__grade").forEach((grupo) => {
    grupo.querySelectorAll(".revelar").forEach((item, indice) => {
      item.style.setProperty("--atraso", `${indice * 0.07}s`);
    });
  });

  const animarContador = (elemento) => {
    const alvo = Number(elemento.dataset.contador);
    if (reduzirMovimento) {
      elemento.textContent = alvo;
      return;
    }
    const duracao = 1200;
    const inicio = performance.now();
    const passo = (agora) => {
      const progresso = Math.min((agora - inicio) / duracao, 1);
      const suavizado = 1 - Math.pow(1 - progresso, 3);
      elemento.textContent = Math.round(alvo * suavizado);
      if (progresso < 1) requestAnimationFrame(passo);
    };
    requestAnimationFrame(passo);
  };

  if ("IntersectionObserver" in window) {
    const observador = new IntersectionObserver((entradas) => {
      entradas.forEach((entrada) => {
        if (!entrada.isIntersecting) return;
        entrada.target.classList.add("revelar--visivel");
        entrada.target.querySelectorAll("[data-contador]").forEach(animarContador);
        observador.unobserve(entrada.target);
      });
    }, { threshold: 0.15, rootMargin: "0px 0px -40px 0px" });
    elementos.forEach((elemento) => observador.observe(elemento));
  } else {
    elementos.forEach((elemento) => elemento.classList.add("revelar--visivel"));
    document.querySelectorAll("[data-contador]").forEach((elemento) => {
      elemento.textContent = elemento.dataset.contador;
    });
  }

  const icones = {
    pernas: '<svg viewBox="0 0 24 24"><path d="M4 20h16M7 20V9l5-5 5 5v11M10 20v-6h4v6"/></svg>',
    peso: '<svg viewBox="0 0 24 24"><path d="M6.5 6.5v11M17.5 6.5v11M3 9v6M21 9v6M6.5 12h11"/></svg>',
    esteira: '<svg viewBox="0 0 24 24"><path d="M3 18h18M5 18l3-10h4l2 4h5"/><circle cx="7" cy="18" r="1.5"/><circle cx="17" cy="18" r="1.5"/></svg>',
    bike: '<svg viewBox="0 0 24 24"><circle cx="6" cy="17" r="3"/><circle cx="18" cy="17" r="3"/><path d="M6 17l4-8h5l3 8M10 9 8 5H6"/></svg>'
  };

  const equipamentos = [
    { nome: "Leg Press 45°", sensor: "SENSOR-001", icone: "pernas", emUso: true },
    { nome: "Supino Reto", sensor: "SENSOR-002", icone: "peso", emUso: false },
    { nome: "Esteira 03", sensor: "SENSOR-003", icone: "esteira", emUso: false },
    { nome: "Bicicleta 01", sensor: "SENSOR-004", icone: "bike", emUso: true }
  ];

  const lista = document.getElementById("painelLista");
  const totalLivres = document.getElementById("painelLivres");
  const totalUso = document.getElementById("painelUso");
  const codigoEmUso = document.getElementById("codigoEmUso");

  const desenhar = (indiceAlterado) => {
    lista.innerHTML = equipamentos.map((item, indice) => `
      <li class="equipamento${item.emUso ? " equipamento--uso" : ""}${indice === indiceAlterado ? " equipamento--mudou" : ""}">
        <span class="equipamento__icone" aria-hidden="true">${icones[item.icone]}</span>
        <span>
          <span class="equipamento__nome">${item.nome}</span>
          <span class="equipamento__sensor">${item.sensor}</span>
        </span>
        <span class="status ${item.emUso ? "status--uso" : "status--livre"}">${item.emUso ? "Em uso" : "Livre"}</span>
      </li>`).join("");
    const emUso = equipamentos.filter((item) => item.emUso).length;
    totalUso.textContent = emUso;
    totalLivres.textContent = equipamentos.length - emUso;
    if (codigoEmUso) codigoEmUso.textContent = String(equipamentos[0].emUso);
  };

  desenhar(-1);

  if (!reduzirMovimento) {
    setInterval(() => {
      const indice = Math.floor(Math.random() * equipamentos.length);
      equipamentos[indice].emUso = !equipamentos[indice].emUso;
      desenhar(indice);
    }, 2600);
  }

  const ano = document.getElementById("ano");
  if (ano) ano.textContent = new Date().getFullYear();
})();
