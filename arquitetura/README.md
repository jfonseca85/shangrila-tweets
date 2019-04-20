## Shangrila - Cotação de Moedas
## Spring MicroServices

Neste projeto foi utilizada a arquitetura baseada em microserviços.

Introdução
O desenvolvimento de serviços Web RESTful foi feito uma combinação de Spring Boot, Spring Web MVC, Spring Web Services e JPA torna ainda mais empolgante trabalhar com essas ferramentas. E é ainda mais criar microsserviços.

Arquiteturas estão se movendo em direção a microservices.

Os serviços Web RESTful são o primeiro passo para o desenvolvimento de grandes microsserviços. O Spring Boot, em combinação com o Spring Web MVC (também chamado de Spring REST), facilita o desenvolvimento de serviços da Web RESTful.

Neste projeto eu utilizei os fundamentos dos serviços RestFul. Implementando esses recursos com vários recursos - controle de versão, tratamento de exceções, documentação (Swagger), autenticação básica (Spring Security), filtragem e HATEOAS. Procurando sem utilizar as melhores práticas na criação de serviços da Web RESTful.

Nesta utilizei Spring (Gerenciamento de Dependência), Spring MVC (ou Spring REST), Spring Boot, Spring Security (Autenticação e Autorização), Spring Boot Actuador (Monitoring), Swagger (Documentation), Maven ( gerenciamento de dependências), Eclipse (IDE), Postman (Cliente de Serviços REST) ​​e Servidor da Web Incorporado Tomcat e MongoDB.

Utilizei o básico sobre microsserviços. Implementando microsserviços usando o Spring Cloud.

## O que eu fiz

Configuração Centralizada do Microservice com o Spring Cloud Config Server
O balanceamento de carga do lado do cliente (Ribbon), o dimensionamento dinâmico (Eureka Naming Server) e um gateway de API (Zuul)
Implementei o rastreamento distribuído para microsserviços com o Spring Cloud Sleuth

## Zipkin
Tolerância a falhas para microsserviços com Zipkin
Monitoramento dos serviços RESTful com o Spring Boot Actuador
Documentação os Serviços Web RESTful com o Swagger
Sempre utilizando as melhores práticas no design de serviços da web RESTful
Usando o Spring Cloud Bus para trocar mensagens sobre atualizações de configuração
Procurei simplificar a comunicação com outros microsserviços usando o Feign REST Client
Ferramentas Utilizadas

## Instalando as ferramentas
- Eclipse & Embedded Maven
- PostMan
- Git Client - https://git-scm.com/
- Rabbit MQ - https://www.rabbitmq.com/download.html


## Instalando Eclipse & Embedded Maven
- Installation Video : https://www.youtube.com/playlist?list=PLBBog2r6uMCSmMVTW_QmDLyASBvovyAO3
- GIT Repository For Installation : https://github.com/in28minutes/getting-started-in-5-steps
- PDF : https://github.com/in28minutes/SpringIn28Minutes/blob/master/InstallationGuide-JavaEclipseAndMaven_v2.pdf

## Instalando Rabbit MQ

#### Windows
- https://www.rabbitmq.com/install-windows.html
- https://www.rabbitmq.com/which-erlang.html
- http://www.erlang.org/downloads
- Video - https://www.youtube.com/watch?v=gKzKUmtOwR4

### Mac
- https://www.rabbitmq.com/install-homebrew.html

## Running Examples
- Download the zip or clone do Repositório Git.
- Unzip o arquivo zip(Caso voce tenha feito Download)
- Abra o Command Prompt e mude o diretorio(cd) para a pasta que contém o pom.xml
- Abra o  Eclipse 
   - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
   - Selecione o projeto correto
- Procure o arquivo do Spring Boot Application (Busque por @SpringBootApplication)
- Clique botão direito e execute Run as Java Application

## Ports

|     Application       |     Port          |
| ------------- | ------------- |
| Limits Service | 8080, 8081, ... |
| Spring Cloud Config Server | 8888 |
|  |  |
| Currency Conversion Update | 8200, 8201, 8202, ... |
| Netflix Eureka Naming Server | 8761 |
| Netflix Zuul API Gateway Server | 8765 |
| Zipkin Distributed Tracing Server | 9411 |


## URLs

|     Application       |     URL          |
| ------------- | ------------- |
| Limits Service | http://localhost:8080/limits POST -> http://localhost:8080/actuator/refresh|
| Spring Cloud Config Server| http://localhost:8888/limits-service/default http://localhost:8888/limits-service/dev |
| Eureka | http://localhost:8761/|
| Zipkin | http://localhost:9411/zipkin/ |
| Spring Cloud Bus Refresh | http://localhost:8080/bus/refresh |

## Esta documentação esta em criacao