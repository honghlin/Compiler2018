
cd "$(dirname "$0")"
export CCHK="java -classpath ./lib/antlr-4.7.1-complete.jar:./bin Main"
cat > program.txt   # save everything in stdin to program.txt

$CCHK
cat program.txt | grep "get" >hasget
if diff empty hasget >null; then
	nasm -felf64 result.nasm 
	gcc -static result.o 
	./a.out > ans.out
	./inputbuilder < ans.out > tmp.c
	./c2nasm.sh tmp.c >null
	cat tmp.asm
else
	cat result.nasm 
fi
