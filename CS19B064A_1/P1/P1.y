%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
extern int yylex();
extern void yyerror(char *);
extern char* yytext;

struct CustomMacro {
    char* identifier;
    char* replacement;
    char** arguments;
    int num_args;
    int macro_type;
};

int macro_count = 0;
#define MAX_ARGS 1000
struct CustomMacro macro_list[MAX_ARGS];

char* get_string(int size){
	char* temp = (char *)malloc(size*sizeof(char));
	return temp;
}

char* replace_string(char* string_array[]){
	int required_size = 0;
	int i= -1;
	while( string_array[++i] != NULL){
		required_size += strlen(string_array[i]);
	}


	char* temp = get_string(required_size+5);
	strcpy(temp,string_array[0]);
	i= 0;
	while( string_array[++i] != NULL){
		strcat(temp,string_array[i]);
	}
	return temp;
}


int is_valid_character(char ch) {
    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_')
        return 1;
    return 0;
}

void initialize_macro(int type, char* idf, char* replace, char** args, int num_args, struct CustomMacro* macro) {
    macro->macro_type = type;

    macro->identifier = get_string(strlen(idf) + 1);
    strcpy(macro->identifier, idf);

    macro->replacement = get_string(strlen(replace) + 1);
    strcpy(macro->replacement, replace);

    macro->arguments = args;
    macro->num_args = num_args;
}

void add_macro(int type, char* idf, char* arglist, char* replace) {
    int i, j;
    
    int index;
    for (index = 0; index < macro_count; index++) {
        if (strcmp(idf, macro_list[index].identifier) == 0) {
            printf("// Failed to parse macrojava code.");
            exit(0);
        }
    }

    int num_args;
    char** args;
    int k, l;
    if (arglist == NULL) {
        num_args = 0;
        args = NULL;
    }
    else {
        num_args = 1;
        i = 0;
        l = strlen(arglist);
        while (i < l) {
            if (arglist[i] == ',')
                num_args++;
            i++;
        }

        args = (char**)malloc((num_args) * sizeof(char*));
        for (i = 0; i < num_args; i++)
            args[i] = get_string(strlen(arglist) + 1);
        k = 0, j = 0;

        for (i = 0; i < l; i++) {
            if (arglist[i] != ',') {
                args[k][j] = arglist[i];
                j++;
            }
            else {
                args[k][j] = '\0';
                k++;
                j = 0;
            }
        }
        args[k][j] = '\0';
    }
    
    initialize_macro(type, idf, replace, args, num_args, &macro_list[macro_count]);
    macro_count++;
}

char* process_macro_replacement(int index) {
    int l = strlen(macro_list[index].replacement);
    char* result = get_string(10000);
    int pos = 0;
    result[0] = '\0';

    char* a = get_string(l + 5);
    int k = 0;
    a[0] = '\0';

    for (int i = 0; i < l; i++) {
        if (!is_valid_character(macro_list[index].replacement[i])) {
            a[k] = '\0';
            for (int j = 0; j < macro_list[index].num_args; j++) {
                if (strcmp(macro_list[index].arguments[j], a) == 0) {
                    strcat(result, macro_list[index].arguments[j]);
                    pos += strlen(macro_list[index].arguments[j]);
                    a[0] = '\0';
                    k = 0;
                    break;
                }
            }
            a[k] = '\0';
            strcat(result, a);
            pos += strlen(a);
            k = 0;
            result[pos++] = macro_list[index].replacement[i];  // Potential issue
            result[pos] = '\0';  // Null-terminate result
        } else {
            a[k++] = macro_list[index].replacement[i];
        }
    }
    return result;
}



char* macro_replace(int type, char* idf, char* arglist) {
    int index, found = 0;
    char* result;
    for (index = 0; index < macro_count; index++) {
        if (type == macro_list[index].macro_type && strcmp(idf, macro_list[index].identifier) == 0) {
            found = 1;
            break;
        }
    }

    if (!found) {
        printf("// Failed to parse macrojava code.");
        exit(0);
    }

    if (arglist == NULL) {
        if (macro_list[index].num_args == 0) {
            result = get_string(strlen(macro_list[index].replacement) + 1);
            strcpy(result, macro_list[index].replacement);
            return result;
        }
        printf("// Failed to parse macrojava code.");
        exit(0);
    }

    int num_args;
    char** args;
    int i, j, k, l;
    if (arglist == NULL) {
        num_args = 0;
        args = NULL;
    }
    else {
        num_args = 1;
        i = 0;
        l = strlen(arglist);
        while (i < l) {
            if (arglist[i] == ',')
                num_args++;
            i++;
        }

        args = (char**)malloc((num_args + 2) * sizeof(char*));
        for (i = 0; i < num_args; i++)
            args[i] = get_string(strlen(arglist) + 1);
        k = 0, j = 0;

        for (i = 0; i < l; i++) {
            if (arglist[i] != ',') {
                args[k][j] = arglist[i];
                j++;
            }
            else {
                args[k][j] = '\0';
                k++;
                j = 0;
            }
        }
        args[k][j] = '\0';
    }

    if (num_args != macro_list[index].num_args) {
        printf("// Failed to parse macrojava code.");
        exit(0);
    }

    result = process_macro_replacement(index);
    return result;
}



%}
%union{
	char* string;
}

%type <string> Goal MacroStar TypeStar MainClass TypeDeclaration MethodStar TypeIdenSemiStar MethodDeclaration CommaTypeIdenStar
%type <string> TypeIdenCommaFirst StatementList Type CommaExprStar ExprCEFirst Statement if_else_block Expression PrimaryExpression
%type <string> Term MacroDefinition CommaIdenStar MacroDefStatement MacroDefExpression Identifier Integer MacroExpression MacroStatement

%token  CLASS PUBLIC STATIC data_VOID MAIN STRING PRINT EXTENDS RETURN
%token  data_INT data_BOOLEAN IF ELSE DO WHILE LENGTH data_TRUE data_FALSE
%token  THIS NEW DEFINE_MACRO SEMICOLON L_PARAN1 R_PARAN1
%token  L_PARAN2 R_PARAN2 L_PARAN3 R_PARAN3
%token  DOT COMMA PLUS MINUS EQUAL AND OR NEQ LEQ GEQ MULTIPLY DIVIDE NEG
%token <string> IDENTIFIER NUMBER
%token YYEOF 0

%nonassoc NO_ELSE
%nonassoc ELSE
%nonassoc NO_EXPR
%nonassoc AND OR LEQ NEQ PLUS MINUS MULTIPLY DIVIDE L_PARAN3 DOT


%start Goal_Start

%%
Goal_Start:		Goal 	YYEOF		{printf("%s",$1);exit(0);};
Goal:  MacroStar MainClass 	TypeStar	{char* temp[] = {$2,$3,NULL};$$=replace_string(temp);};

MacroStar: MacroStar MacroDefinition 
		|  				{}	;

TypeStar: TypeDeclaration TypeStar	{char* temp[]={$1,$2,NULL};$$=replace_string(temp); }
		| 							{char* temp[]={"",NULL};$$=replace_string(temp); };

MainClass: CLASS Identifier L_PARAN1 PUBLIC STATIC data_VOID MAIN L_PARAN2 STRING L_PARAN3 R_PARAN3 Identifier R_PARAN2 L_PARAN1 PRINT L_PARAN2 Expression R_PARAN2 SEMICOLON R_PARAN1 R_PARAN1	{
			char* temp[]={"class ",$2,"{","public static void main (String[] ",$12,"){System.out.println(",$17,");}}",NULL};$$=replace_string(temp);};

TypeDeclaration: CLASS Identifier L_PARAN1 TypeIdenSemiStar MethodStar R_PARAN1 {char* temp[]={"class ",$2,"{",$4,$5,"}",NULL};$$=replace_string(temp); }
				| CLASS Identifier EXTENDS Identifier L_PARAN1 TypeIdenSemiStar MethodStar R_PARAN1 { char*temp[] ={"class ",$2," extends ",$4,"{",$6,$7,"}",NULL};$$=replace_string(temp);};

MethodStar: MethodDeclaration MethodStar	{ char* temp[]={$1,$2,NULL};$$=replace_string(temp);}
		|		{ char* temp[]={"",NULL};$$=replace_string(temp);};

TypeIdenSemiStar: TypeIdenSemiStar Type Identifier SEMICOLON {char* temp[]={$1,$2,$3,";",NULL};$$=replace_string(temp); }
		| 	{char* temp[]={"",NULL};$$=replace_string(temp); };

MethodDeclaration: PUBLIC Type Identifier L_PARAN2 TypeIdenCommaFirst R_PARAN2 L_PARAN1 TypeIdenSemiStar StatementList RETURN Expression SEMICOLON R_PARAN1 {	
					char* temp[] = {"public ",$2,$3,"(",$5,")","{",$8,$9,"return ",$11,";}",NULL};$$=replace_string(temp);};

CommaTypeIdenStar: COMMA Type Identifier CommaTypeIdenStar	{
			char* temp[] = {",",$2,$3,$4,NULL};$$=replace_string(temp);}
		| 	{char* temp[] = {"",NULL}; $$=replace_string(temp);};

TypeIdenCommaFirst: Type Identifier CommaTypeIdenStar	{char* temp[] = {$1,$2,$3,NULL};$$=replace_string(temp);}
		|	{ char* temp[] = {"",NULL}; $$=replace_string(temp); };

StatementList: 	Statement StatementList	{char* temp[] = {$1,$2,NULL}; $$=replace_string(temp); }
			| 	{ char* temp[] = {"",NULL}; $$=replace_string(temp);};

Type: data_INT L_PARAN3 R_PARAN3 {char* temp[] = {"int[] ",NULL}; $$=replace_string(temp); }
	| data_BOOLEAN	{ char* temp[] = {"boolean ",NULL}; $$=replace_string(temp);}
	| data_INT 		{char* temp[] = {"int ",NULL}; $$=replace_string(temp); }
	| Identifier { char* temp[] = {$1," ",NULL}; $$=replace_string(temp);;/*char* temp[] = {$1," ",NULL}; $$=replace_string(temp);*/};

CommaExprStar: CommaExprStar COMMA Expression {char* temp[] = {$1,",",$3,NULL}; $$=replace_string(temp); }
		|	{$$=(char*)malloc(2*sizeof(char));strcpy($$,""); };

ExprCEFirst: Expression CommaExprStar	{char* temp[] = {$1,$2,NULL}; $$=replace_string(temp); }
		|	{char* temp[] = {"",NULL}; $$=replace_string(temp); };

Statement: L_PARAN1 StatementList R_PARAN1 	{char* temp[] = {"{",$2,"}",NULL}; $$=replace_string(temp);  }
		|  PRINT L_PARAN2 Expression R_PARAN2 SEMICOLON 	{ char* temp[] = {"System.out.println(",$3,");",NULL}; $$=replace_string(temp);}
		|  Identifier EQUAL Expression SEMICOLON	{ char* temp[] = {$1,"=",$3,";",NULL}; $$=replace_string(temp); }
		|  Identifier L_PARAN3 Expression R_PARAN3 EQUAL Expression SEMICOLON  {char* temp[] = {$1,"[",$3,"]=",$6,";",NULL}; $$=replace_string(temp); }
		|  DO Statement WHILE L_PARAN2 Expression R_PARAN2  {char* temp[] = {"do ",$2,"while(",$5,")",NULL}; $$=replace_string(temp); }
		|  if_else_block	{char* temp[] = {$1,NULL}; $$=replace_string(temp); }
		|  WHILE L_PARAN2 Expression R_PARAN2 Statement	{char* temp[] = {"while(",$3,")",$5,NULL}; $$=replace_string(temp); }
		|  MacroStatement	{$$=$1; };


MacroStatement: Identifier L_PARAN2 Expression CommaExprStar R_PARAN2 SEMICOLON {char* temp[] = {$3,$4,NULL}; char* temp2=replace_string(temp); $$=macro_replace(1,$1,temp2);}
				|Identifier L_PARAN2 R_PARAN2 SEMICOLON {$$=macro_replace(1,$1,NULL);};

if_else_block: IF L_PARAN2 Expression R_PARAN2 Statement ELSE Statement	{char* temp[] = {"if(",$3,")",$5,"else ",$7,NULL}; $$=replace_string(temp);}
			|  IF L_PARAN2 Expression R_PARAN2 Statement	%prec NO_ELSE {char* temp[] = {"if(",$3,")",$5,NULL}; $$=replace_string(temp);};


MacroExpression: Identifier L_PARAN2 Expression CommaExprStar R_PARAN2 {char* temp[] = {$3,$4,NULL}; char* temp2=replace_string(temp); $$=macro_replace(0,$1,temp2);}
				|Identifier L_PARAN2 R_PARAN2 {$$=macro_replace(0,$1,NULL);};

Expression: PrimaryExpression AND PrimaryExpression		{char* temp[] = {$1,"&&",$3,NULL}; $$=replace_string(temp); };
		|	PrimaryExpression OR PrimaryExpression		{ char* temp[] = {$1,"||",$3,NULL}; $$=replace_string(temp);};
		|	PrimaryExpression LEQ PrimaryExpression {char* temp[] = {$1,"<=",$3,NULL}; $$=replace_string(temp); };
		|	PrimaryExpression NEQ PrimaryExpression { char* temp[] = {$1,"!=",$3,NULL}; $$=replace_string(temp);}	;	
		|	PrimaryExpression PLUS PrimaryExpression		{char* temp[] = {$1,"+",$3,NULL}; $$=replace_string(temp); };
		|	PrimaryExpression MINUS PrimaryExpression		{char* temp[] = {$1,"-",$3,NULL}; $$=replace_string(temp);};
		|	PrimaryExpression MULTIPLY PrimaryExpression		{char* temp[] = {$1,"*",$3,NULL}; $$=replace_string(temp);};
		|	PrimaryExpression DIVIDE PrimaryExpression			{char* temp[] = {$1,"/",$3,NULL}; $$=replace_string(temp); };
		|	PrimaryExpression L_PARAN3 PrimaryExpression R_PARAN3	{char* temp[] = {$1,"[",$3,"]",NULL}; $$=replace_string(temp); };
		|	PrimaryExpression DOT LENGTH		{char* temp[] = {$1,".length",NULL}; $$=replace_string(temp);};
		|	PrimaryExpression	%prec NO_EXPR		{char* temp[] = {$1,NULL}; $$=replace_string(temp);};
		|	PrimaryExpression DOT Identifier L_PARAN2 ExprCEFirst R_PARAN2 {char* temp[] = {$1,".",$3,"(",$5,")",NULL}; $$=replace_string(temp);};
		|   MacroExpression	{$$=$1; };


PrimaryExpression: 	NEW data_INT L_PARAN3 Expression R_PARAN3	{ char* temp[] = {"new int[",$4,"]",NULL}; $$=replace_string(temp);}
				|	NEW Identifier L_PARAN2 R_PARAN2	{char* temp[] = {"new ",$2,"()",NULL}; $$=replace_string(temp); }
				|	L_PARAN2 Expression R_PARAN2	{char* temp[] = {"(",$2,")",NULL}; $$=replace_string(temp);  }
				|	Term	{$$=$1;}
				|	NEG Expression 	{char* temp[] = {"!",$2,NULL}; $$=replace_string(temp); };

Term:	Integer 	{$$=$1;}
	|	data_TRUE		{char* temp[] = {"true",NULL}; $$=replace_string(temp); }
	| 	data_FALSE		{char* temp[] = {"false",NULL}; $$=replace_string(temp);}
	|	Identifier	{$$=$1;}
	|	THIS		{ char* temp[] = {"this",NULL}; $$=replace_string(temp);};

MacroDefinition: MacroDefExpression 
			|	 MacroDefStatement ;

CommaIdenStar: CommaIdenStar COMMA Identifier	{ char* temp[] = {$1,",",$3,NULL}; $$=replace_string(temp); }
		|	{$$=(char*)malloc(2*sizeof(char));strcpy($$,"");};

MacroDefStatement: DEFINE_MACRO Identifier L_PARAN2 Identifier CommaIdenStar R_PARAN2 L_PARAN1 StatementList R_PARAN1 {
					char* temp[] = {$4,$5,NULL}; char* temp3 = replace_string(temp);
					char* temp2[] = {"{",$8,"}",NULL}; char* temp4 = replace_string(temp2);
					add_macro(1,$2,temp3,temp4);}
				|  DEFINE_MACRO Identifier L_PARAN2 R_PARAN2 L_PARAN1 StatementList R_PARAN1 {
					char* temp2[] = {"{",$6,"}",NULL}; char* temp4 = replace_string(temp2);
					add_macro(1,$2,NULL,temp4);
};

MacroDefExpression: DEFINE_MACRO Identifier L_PARAN2 Identifier CommaIdenStar R_PARAN2 L_PARAN2 Expression R_PARAN2	{
					char* temp[] = {$4,$5,NULL};char* temp3 = replace_string(temp);
					char* temp2[] = {"(",$8,")",NULL};char* temp4 = replace_string(temp2);
					add_macro(0,$2,temp3,temp4);}
				|	DEFINE_MACRO Identifier L_PARAN2 R_PARAN2 L_PARAN2 Expression R_PARAN2	{
					char* temp2[] = {"(",$6,")",NULL};char* temp4 = replace_string(temp2);
					add_macro(0,$2,NULL,temp4);
};


Identifier: IDENTIFIER 	{char* temp[] = {$1,NULL};$$=replace_string(temp);};

Integer: NUMBER 	{char* temp[] = {$1,NULL}; $$=replace_string(temp);};