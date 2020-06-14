# ES2-2020-EIC2-26 
André Barão 78352 

Henrique Gaspar 82304 

João Faria 78980 

Nichal Narotamo 82247 

Pedro Fonseca 82131 

Stefan Tataru 78965 


Funcionalidades não implementadas:

O grupo não conseguiu correr ficheiros .jar dentro do container.




Funcionalidades com erros/parcialmente implementadas:

No requisito 2, para efetuar a verificação a cada duas horas, não conseguimos por o ficheiro de testes a correr dentro do container
(devido a falta de dependências).

O site ContactUs não funciona da melhor forma pois depende de um serviço SMTP que não é 100% fiável.

Como para o requisito 2 é preciso verificar a disponibilidade do email, 





Instruções de utilização:

Fazer o unzip da pasta mysql, e copiar o conteúdo que está dentro dessa pasta para C:/Users/Wordpress/db_data

Fazer o unzip da pasta html, e copiar o conteúdo que está dentro dessa pasta para C:/Users/Wordpress/html

Copiar o ficheiro docker-compose-yml para dentro da pasta C:/Users/Wordpress

Abrir o docker e depois de posicionar-se na diretoria C:/Users/Wordpress, correr o comando docker-compose up -d

(Se se estiver a usar o docker-toolbox deve-se aceder ao site usando o endereço: 192.168.99.100. Enquanto que se estiver o docker através da linha de comandos do windows deve se usar o endereço: localhost:80.)



