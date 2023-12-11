# Makefile for Java Program

# Compilador Java
JAVAC = javac

# Opções do compilador
JAVACFLAGS = -d bin -sourcepath src

# Arquivos fonte
SOURCES = $(wildcard src/**/*.java)

# Nome do programa principal
MAIN = Main

# Diretório para os arquivos compilados
BIN_DIR = bin

# Comando para executar o programa
JAVA = java
JAVAFLAGS = -cp $(BIN_DIR)

all: $(BIN_DIR) $(MAIN)

$(BIN_DIR):
	mkdir $(BIN_DIR)

$(MAIN): $(SOURCES)
	$(JAVAC) $(JAVACFLAGS) $^

run: all
	$(JAVA) $(JAVAFLAGS) $(MAIN)

clean:
	rm -rf $(BIN_DIR)

.PHONY: all run clean
