cd "$(dirname "$0")"
cat > test.mx
java -classpath ./lib/antlr-4.7.1-complete.jar:./bin cn.kzoacn.Builder

cat program.txt | grep "get" >hasget
if diff empty hasget >null; then
	nasm -felf64 result.nasm 
	gcc -static result.o 
	./a.out > ans.out
	./inputbuilder < ans.out > tmp.c
	./c2nasm.sh tmp.c 
	cat tmp.c.nasm
else
	cat result.nasm 
fi
