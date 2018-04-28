grammar Ms;

compilationUnit
    : (variableDefinition | functionDefinition | classDefinition)* EOF
    ;

variableDefinition
    : typeType Identifier ('=' expression)? ';'
    ;

functionDefinition
    : ret=typeType? name=Identifier '(' (parameter (',' parameter)*)? ')'
               block
    ;

classDefinition
    : CLASS name=Identifier '{' (functionDefinition | variableDefinition)* '}'
    ;

block
    : '{' statement* '}'
    ;

typeType
    : (Identifier | primitiveType) ('[' ']')*
    ;

primitiveType
    : type = (BOOL | INT | STRING | VOID)
    ;

parameter
    : typeType Identifier
    ;

statement
    : block                                                              # blockStmt
    | variableDefinition                                                 # varDefStmt
    | IF '(' expression ')' statement (ELSE statement)?                  # ifStmt
    | FOR '(' init=expression? ';' cond=expression? ';'
                                     incr=expression? ')' statement      # forStmt
    | WHILE'(' expression ')' statement                                  # whileStmt
    | RETURN expression? ';'                                             # returnStmt
    | BREAK ';'                                                          # breakStmt
    | CONTINUE ';'                                                       # continueStmt
    | expression ';'                                                     # exprStmt
    | ';'                                                                # blankStmt
    ;

expressionList
    : expression (',' expression)*
    ;

expression
    : primary                                            # primaryExpr
    | expression '.' Identifier                          # memberExpr
    | NEW creator                                        # newExpr
    | expression '[' expression ']'                      # arefExpr
    | expression '(' expressionList? ')'                 # funcallExpr
    | expression op=('++' | '--')                        # suffixExpr
    | op=('+' | '-' | '++' | '--') expression            # prefixExpr
    | op=('~' | '!' ) expression                         # prefixExpr
    | expression op=('*' | '/' | '%') expression         # binaryExpr
    | expression op=('+' | '-') expression               # binaryExpr
    | expression op=('<<' | '>>') expression             # binaryExpr
    | expression op=('<' | '>' | '>=' | '<=') expression # binaryExpr
    | expression op=('==' | '!=' ) expression            # binaryExpr
    | expression op='&' expression                       # binaryExpr
    | expression op='^' expression                       # binaryExpr
    | expression op='|' expression                       # binaryExpr
    | expression '&&' expression                         # logicalAndExpr
    | expression '||' expression                         # logicalOrExpr
    | <assoc=right> expression '=' expression            # assignExpr
    ;

primary
    : '(' expression ')'   # subExpr
    | THIS                 # thisExpr
    | Identifier           # variableExpr
    | literal              # literalExpr
    ;

literal
    : DecimalInteger          # DecIntegerConst
    | StringLiteral           # StringConst
    | value=('true'|'false')  # boolConst
    | 'null'                  # nullConst
    ;

creator
    : (Identifier | primitiveType) ('[' expression ']')+ ('[' ']')+
                                              ('[' expression ']')+ # errorCreator
    | (Identifier | primitiveType) ('[' expression ']')+ ('[' ']')* # arrayCreator
    | Identifier                                                    # nonarrayCreator
    ;

StringLiteral
    : '"' StringCharacter* '"'
    ;

fragment
StringCharacter
    : ~["\\\n\r]
    | '\\' ["n\\]
    ;

Identifier
    : [a-zA-Z_] [a-zA-Z_0-9]*
    ;

DecimalInteger
    : [1-9] [0-9]*
    | '0'
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

CLASS:  'class';
IF:     'if';
ELSE:   'else';
WHILE:  'while';
FOR:    'for';
BREAK:  'break';
CONTINUE:   'continue';
RETURN: 'return';
THIS:   'this';
NEW:    'new';
STRING: 'string';
INT:    'int';
BOOL:   'bool';
VOID:   'void';