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

-- A partir daqui, conectar no banco gerenciadorprojetos como usuário gerenciador
-- e executar o restante do script.

------------------------------------------------------
-- Tabela de membros (vindos da API externa)
------------------------------------------------------

CREATE TABLE IF NOT EXISTS membro (
    id                   BIGSERIAL       PRIMARY KEY,
    nome                 VARCHAR(150)    NOT NULL,
    atribuicao           VARCHAR(50)     NOT NULL, -- ex: "funcionario", "gerente", etc.
    identificador_externo VARCHAR(100),  -- id retornado pela API mockada, se precisar
    ativo                BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em            TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_membro_atribuicao
    ON membro (atribuicao);

------------------------------------------------------
-- Tabela de projetos
------------------------------------------------------

CREATE TABLE IF NOT EXISTS projeto (
    id                  BIGSERIAL       PRIMARY KEY,
    nome                VARCHAR(200)    NOT NULL,
    data_inicio         DATE            NOT NULL,
    previsao_termino    DATE            NOT NULL,
    data_termino_real   DATE,
    orcamento_total     NUMERIC(15,2)   NOT NULL,
    descricao           TEXT,
    gerente_id          BIGINT          NOT NULL,
    status              VARCHAR(30)     NOT NULL,  -- mapeado para enum no código
    risco               VARCHAR(20)     NOT NULL,  -- mapeado para enum no código
    criado_em           TIMESTAMP       NOT NULL DEFAULT NOW(),
    atualizado_em       TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_projeto_gerente
        FOREIGN KEY (gerente_id) REFERENCES membro (id)
);

CREATE INDEX IF NOT EXISTS idx_projeto_status
    ON projeto (status);

CREATE INDEX IF NOT EXISTS idx_projeto_gerente
    ON projeto (gerente_id);

------------------------------------------------------
-- Tabela de alocação de membros em projetos
------------------------------------------------------

CREATE TABLE IF NOT EXISTS projeto_membro (
    id              BIGSERIAL   PRIMARY KEY,
    projeto_id      BIGINT      NOT NULL,
    membro_id       BIGINT      NOT NULL,
    papel           VARCHAR(100),
    alocado_em      TIMESTAMP   NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_projeto_membro_projeto
        FOREIGN KEY (projeto_id) REFERENCES projeto (id) ON DELETE CASCADE,
    CONSTRAINT fk_projeto_membro_membro
        FOREIGN KEY (membro_id)  REFERENCES membro  (id),
    CONSTRAINT uq_projeto_membro_unique
        UNIQUE (projeto_id, membro_id)
);

CREATE INDEX IF NOT EXISTS idx_projeto_membro_membro
    ON projeto_membro (membro_id);

CREATE INDEX IF NOT EXISTS idx_projeto_membro_projeto
    ON projeto_membro (projeto_id);

-- As regras de status, risco e limites de alocação
-- serão tratadas na camada de serviço com enums em Java.