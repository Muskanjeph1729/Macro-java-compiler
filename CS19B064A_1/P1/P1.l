%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "P1.tab.h"

void yyerror();
int yyparse(void);
%}

DIGIT [0-9]*
ID [a-zA-Z_][a-zA-Z0-9_]*


%%

"class"					{return CLASS;}
"public"				{return PUBLIC;}
"static"				{return STATIC;}
"void"					{return data_VOID;}
"main"					{return MAIN;}
"String"				{return STRING;}
"System.out.println"	{return PRINT;}
"extends"				{return EXTENDS;}
"return"				{return RETURN;}
"int"					{return data_INT;}
"boolean"				{return data_BOOLEAN;}
"if"					{return IF;}
"else"					{return ELSE;}
"do"					{return DO;}
"while"					{return WHILE;}
"length"				{return LENGTH;}
"true"					{return data_TRUE;}
"false"					{return data_FALSE;}
"this"					{return THIS;}
"new"					{return NEW;}
"#define"				{return DEFINE_MACRO;}
[a-zA-Z_][a-zA-Z0-9_]*					{yylval.string=strdup(yytext);
						 	return IDENTIFIER;}
0|([1-9]{DIGIT})			{yylval.string=strdup(yytext);
						return NUMBER; }

";"						{return SEMICOLON;}
"{"						{return L_PARAN1;}
"}"						{return R_PARAN1;}
"("						{return L_PARAN2;}
")"						{return R_PARAN2;}
"["						{return L_PARAN3;}
"]"						{return R_PARAN3;}
"="						{return EQUAL;}
"&&"					{return AND;}
"||"					{return OR;}
"!="					{return NEQ;}
"<="					{return LEQ;}
">="					{return GEQ;}
"+"						{return PLUS;}
"-"						{return MINUS;}
"*"						{return MULTIPLY;}
"/"						{return DIVIDE;}
"."						{return DOT;}
"!"						{return NEG;}
","						{return COMMA;}
\/\/.*\n				;
\/\*.*\*\/	;
[ \t\n]					;
.						{return 1;}

%%

void yyerror() { printf("// Failed to parse macrojava code.");}
int yywrap(void) { return 1; } 

int main(){
	yyparse();
}