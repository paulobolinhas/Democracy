# CONTRIBUTORS

- Paulo Bolinhas fc56300

- João Santos fc57103

PT

# NOTAS (ANTES DE USAR):

---> Este projeto foi feito no gitlab.

---> Ler 'Sobre o projeto.pdf' para saber em que consiste este projeto.

---> Consultar a pasta docs para conhecer o relatório do projeto.

# COMO USAR:

# Inicializar a aplicação desktop

Devido a problemas com dependências, não foi possível gerar corretamente o script client.sh, pelo que a execução da aplicação desktop deve seguir 
os seguintes passos:

    0. Assumindo que já foi executado o comando `sh run.sh`
    
    No eclipse, depois do import do projeto:
    1. import javafx library (com os jars javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media, javafx.swing, javafx.web,       javafx-swt)

    2. Configure build path > run configurations > VM arguments: --module-path "{caminho da instalação da library javafx}/openjfx-20.0.1_linux-x64_bin-sdk/javafx-sdk-20.0.1/lib" --add-modules javafx.controls,javafx.fxml

    3. Configure build path > Source > Adicionar pasta desktop_app (indicar a main relativa à aplicação desktop)

    4. Run as > Java Application
-----
# Projecto de Referência de SpringBoot

## O que fazer

Este projecto serve para verificar que todas as dependências necessárias ao projecto estão a funcionar.

## Dependências

Este projecto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.

### Windows

Instalar [Docker Desktop](https://docs.docker.com/desktop/install/windows-install/) e [Python 3](https://www.python.org/ftp/python/3.11.2/python-3.11.2-amd64.exe)

### Ubuntu

```
sudo apt-get python3 python3-pip
```

Seguir [esta página](https://docs.docker.com/engine/install/ubuntu/) e os quatro passos listados [aqui](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user).

### Fedora

```
sudo dnf python3 python3-pip
```

Seguir [esta página](https://docs.docker.com/engine/install/fedora/) e os quatro passos listados [aqui](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user).


## Primeiro passo

Deve correr os comandos listados no ficheiro `setup.sh`.

## Segundo passo

Deve correr `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres

## Terceiro passo

Abrir http://localhost:8080 num browser e verificar que aparece a string "Olá Mundo".

Pode ainda confirmar que consegue ver um objecto JSON em http://localhost:8080/api/author/1


# FAQ

## Preciso de `sudo` para correr o `run.sh`
Tenta correr `sudo usermod -aG docker $USER` seguido de um log-out na máquina.
Ou tentar [desta forma](https://www.digitalocean.com/community/questions/how-to-fix-docker-got-permission-denied-while-trying-to-connect-to-the-docker-daemon-socket)

## O Docker não instala em ubuntu.

Tentar [desta forma](https://askubuntu.com/a/1411717).

## O `run.sh` não está a correr no meu macos m1.

Tentar correr `docker ps`. Se não funcionar, [tentar isto](https://stackoverflow.com/a/68202428/28516).
Confirmar também que está instalado o Docker Desktop (`brew install --cask docker`) e não apenas a command-line tool (`brew install docker`). A aplicação Docker deve também estar a correr (icon na menubar).


## Estou em windows e o `bash setup.sh` não funciona

Correr numa bash (tanto a Git Bash, MSys2 bash ou WSL em linux) e não na Powershell, nem no CMD.exe.

## `docker compose` não funciona

`docker compose` é o comando da última versão de docker. `docker-compose` é a versão antiga. Devem actualizar o docker.
