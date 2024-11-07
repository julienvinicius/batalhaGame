GitHub:https://github.com/julienvinicius/batalhaGame


Trabalho de Melhoria de Projeto Antigo
1- Projeto Escolhido - Game

Escolhi um projeto desenvolvido anteriormente, um jogo em Java onde o jogador controla uma nave que se movimenta na tela. Esse projeto tem múltiplos arquivos, incluindo uma classe principal que gerencia o jogo (GameFrame), classes auxiliares para controle de eventos e recursos, e uma classe para a nave.

2. melhorias a serem feitas
Ponto de Melhoria 1: Gerenciamento de Recursos como  (Imagens, Sons)

Problema: Atualmente, cada recurso é carregado diretamente nas classes onde é usado. Isso pode levar a duplicação de código e problemas de performance, já que o mesmo recurso pode ser carregado múltiplas vezes.

Padrão de Projeto: Singleton

- Usar o padrão Singleton para criar uma classe de gerenciamento de recursos garantiria que cada recurso (como imagens e sons) seja carregado apenas uma vez e possa ser reutilizado em todo o jogo.



Ponto de Melhoria 2: Movimentação da Nave

Problema: A lógica de movimentação está diretamente implementada dentro da classe GameFrame, o que reduz a flexibilidade para alterar o comportamento ou criar diferentes tipos de movimentos.



Padrão de Projeto: Strategy

- O padrão Strategy permite que a lógica de movimentação da nave seja encapsulada e trocada facilmente, possibilitando a criação de diferentes estratégias de movimentação como movimento com aceleração, movimento com inércia.



Ponto de Melhoria 3: Centralização de Eventos de Teclado

Problema: A lógica de captura e processamento de teclas está dispersa, dificultando a adição de novos controles e a alteração dos existentes.

Padrão de Projeto: Observer

- O padrão Observer permite que uma classe central controle os eventos de teclado e notifique outras classes interessadas, como a nave, facilitando a gestão de eventos de entrada.



Ponto de Melhoria 4: Ciclo de Atualização e Desenho

Problema: O ciclo de atualização e renderização está diretamente acoplado na GameFrame, dificultando o teste e a manutenção.

Padrão de Projeto: Template Method

- O Template Method permite definir o esqueleto do ciclo de jogo em uma classe abstrata, possibilitando a especialização de partes específicas sem alterar a estrutura do loop.



Ponto de Melhoria 5: Configuração e Personalização do Jogo

Problema: Não há flexibilidade para ajustar as configurações do jogo, como a velocidade da nave ou o tamanho da tela, sem modificar o código.

Padrão de Projeto: Factory

- Utilizar o padrão Factory permite encapsular a criação de objetos de configuração, facilitando ajustes e personalizações sem alterar o código principal.




