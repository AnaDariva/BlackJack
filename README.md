# Projeto para Aula de Aplicações Distribuídas e Concorrentes

**Linguagem**: Java
**IDE**: IntelliJ IDEA  

<h2 align="left"> Language & Tools: </h2>

<p align="left">

  <!-- Java -->
  <a href="https://www.java.com" target="_blank" rel="noreferrer">
      <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="Java" width="40" height="40"/>
  </a>
</p>

## 🎮 Jogo 21 – Blackjack com Java RMI

Este projeto desenvolve um jogo de cartas Blackjack (21) utilizando a tecnologia Java RMI (Remote Method Invocation). O sistema é baseado no modelo cliente-servidor, onde o jogador conecta-se remotamente ao servidor para jogar partidas individuais contra o dealer.

## 📌 Funcionalidades

✅ Registro e comunicação remota via Java RMI  
✅ Conexão do cliente ao servidor de jogo  
✅ Distribuição automática de cartas para jogador e dealer  
✅ Cálculo de pontuação:
- Cartas 2 a 10 valem seu próprio valor  
- Valetes (J), Damas (Q) e Reis (K) valem 10 pontos cada  
- O Ás (A) pode valer 1 ou 11 pontos, conforme a situação  

✅ Ações disponíveis para o jogador:
- Pedir carta (Hit)  
- Parar (Stand)  

✅ Lógica automática do dealer:
- O dealer revela a segunda carta e continua pegando cartas até atingir no mínimo 17 pontos  

✅ Identificação dos resultados:
- Vitória  
- Derrota  
- Empate  
- Estouro (quando a pontuação ultrapassa 21)  

✅ Registro de estatísticas: vitórias, derrotas e empates  
✅ Possibilidade de jogar várias rodadas consecutivas  

## 🛠️ Tecnologias Utilizadas

- Linguagem: Java  
- Framework: Java RMI  
- IDE: IntelliJ IDEA  
- Paradigma: Programação Orientada a Objetos  

## 📂 Estrutura do Projeto

📦 jogo21-rmi  
┣ 📂 src  
┃ ┣ 📂 br.edu.utfpr.pb.pw44s.server &nbsp;&nbsp;# Implementação do servidor (GameServiceImpl, ServerMain)  
┃ ┣ 📂 br.edu.utfpr.pb.pw44s.client &nbsp;&nbsp;# Implementação do cliente (ClientMain)  
┃ ┗ 📂 br.edu.utfpr.pb.pw44s.common &nbsp;&nbsp;# Interfaces e classes compartilhadas (GameService, Carta, Jogador)  
┗ README.md &nbsp;&nbsp;# Documentação  

## 👩‍💻 Autor

**Ana Luisa Dariva Ramos – Acadêmica de Análise e Desenvolvimento de Sistemas na UTFPR-PB**
