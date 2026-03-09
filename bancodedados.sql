-- 1) Criar o usuário
CREATE USER gerenciador WITH ENCRYPTED PASSWORD 'gerenciador';

-- 2) Criar o banco de dados
CREATE DATABASE gerenciadorprojetos
    WITH OWNER = gerenciador
         ENCODING = 'UTF8'
         LC_COLLATE = 'pt_BR.UTF-8'
         LC_CTYPE = 'pt_BR.UTF-8'
         TEMPLATE = template0;

-- (opcional) garantir privilégios
GRANT ALL PRIVILEGES ON DATABASE gerenciadorprojetos TO gerenciador;