set -e
cd "$(dirname "$0")"
cat > program.txt
java -classpath ./lib/antlr-4.7.1-complete.jar:./bin  Main
cat result.nasm
