# ES2-2020-EIC2-26 
André Barão 78352 afdbo@iscte-iul.pt

Henrique Gaspar 82304 hapgr@iscte-iul.pt

João Faria 78980 jmjmf@iscte-iul.pt

Nichal Narotamo 82247 nanok@iscte-iul.pt

Pedro Fonseca 82131 pgvbm@iscte-iul.pt

Stefan Tataru 78965 stunt@iscte-iul.pt


 **Funcionalidades não implementadas:**

O grupo não conseguiu correr ficheiros Junit dentro do container.




**Funcionalidades com erros/parcialmente implementadas:**

No requisito 2, para efetuar a verificação a cada duas horas, não conseguimos por o ficheiro de testes a correr dentro do container
(devido a falta de dependências).

O site ContactUs não funciona da melhor forma pois depende de um serviço SMTP que não é 100% fiável.

Como para o requisito 2 é preciso verificar a disponibilidade do email, será preciso enviar "à mão" um email para o recetor.





**Instruções de utilização:**

Copiar o conteúdo que está dentro da pasta mysql para C:/Users/Wordpress/db_data

Copiar a pasta html para C:/Users/Wordpress/

Copiar a pasta cgi-bin para C:/Users/wordpress/

Neste ponto deverão existir 3 pastas na diretoria C:/Users/Wordpress/ .

Copiar o ficheiro docker-compose.yml para dentro da pasta C:/Users/Wordpress (as imagens são descarregadas do dockerHub)

Abrir o docker e depois de posicionar-se na diretoria C:/Users/Wordpress, correr o comando docker-compose up -d

(Se se estiver a usar o docker-toolbox deve-se aceder ao site usando o endereço: 192.168.99.100. Enquanto que se estiver o docker através da linha de comandos do windows deve se usar o endereço: localhost:80.)



