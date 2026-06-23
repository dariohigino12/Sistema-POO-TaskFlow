# TaskFlow — Sistema de Gerenciamento de Tarefas

Sistema de console em **Java 17+**, desenvolvido com **POO** (herança, polimorfismo,
encapsulamento, composição, abstração), para gerenciamento de tarefas pessoais,
acadêmicas e profissionais.

## Funcionalidades implementadas

### Tipos de tarefa (herança + polimorfismo)
- **TarefaSimples** — tarefa básica com rótulo opcional
- **TarefaRecorrente** — tarefa com frequência de repetição (dias) e cálculo automático da próxima ocorrência
- **TarefaComPrazo** — tarefa independente com data limite e verificação de atraso

### Operações CRUD (para cada tipo)
- Criação, edição, exclusão e alteração de status (concluir/reabrir)
- Listagem: todas, pendentes, concluídas, por prioridade/peso
- Tarefas com prazo: listagem de atrasadas

### Lembretes
- Criação e exclusão de lembretes com data/hora
- Listagem de lembretes do usuário

### Usuários
- Cadastro com validação de nome, e-mail (regex) e senha mínima
- Login com proteção contra enumeração de credenciais
- Controle de propriedade: cada usuário só acessa suas próprias tarefas/lembretes

## Conceitos de POO aplicados

| Conceito | Onde |
|----------|------|
| **Herança** | `TarefaSimples extends Tarefa`, `TarefaRecorrente extends Tarefa` |
| **Abstração** | `Tarefa` é classe abstrata com método `calcularPeso()` |
| **Polimorfismo** | Listagens misturam TarefaSimples e TarefaRecorrente; `calcularPeso()` tem lógica diferente em cada subclasse |
| **Encapsulamento** | Transições de estado internas (`concluir`/`reabrir`); campos protegidos com getters/setters |
| **Composição** | Serviços recebem repositórios via construtor (DI manual); `Tarefa` usa `Prioridade` e `StatusTarefa` |
| **Classes independentes** | `TarefaComPrazo` e `Lembrete` são classes próprias, sem herança de `Tarefa` |

## Arquitetura (organização em camadas)

```
com/taskflow
├── model        → Tarefa (abstract), TarefaSimples, TarefaRecorrente,
│                  TarefaComPrazo, Lembrete, Usuario, Prioridade, StatusTarefa
├── exception    → Exceções de negócio (checked) e de validação (runtime)
├── repository   → TarefaRepository, TarefaComPrazoRepository,
│                  LembreteRepository, UsuarioRepository
├── service      → TarefaService, UsuarioService (regras de negócio)
└── app          → Main (menus de console / interface com o usuário)
```

A separação **model → repository → service → app** segue o princípio de
responsabilidade única: o `Main` não conhece regras de negócio, o `Service`
não conhece detalhes de armazenamento, e o `Repository` não conhece regras
de validação.

## Como compilar e executar

Pré-requisito: JDK 17 ou superior instalado.

```bash
# Na raiz do projeto
javac -d out $(find . -name "*.java")
java -cp out com.taskflow.app.Main
```

No Windows (PowerShell), substitua o `find` por:

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java -Path .).FullName
java -cp out com.taskflow.app.Main
```

## Observações

- Os dados são armazenados **em memória** (mapas), ou seja, são
  perdidos ao finalizar o programa. A arquitetura em camadas (repository)
  foi pensada para que, futuramente, seja simples substituir o
  armazenamento em memória por um banco de dados (JDBC) ou arquivo.
- O projeto não usa nenhuma biblioteca externa: apenas Java puro (JDK
  padrão).
- `TarefaComPrazo` é uma classe independente (não herda de `Tarefa`),
  conforme o diagrama de classes UML do projeto.
- Cada tipo de tarefa implementa `calcularPeso()` de forma diferente,
  demonstrando polimorfismo na ordenação.
