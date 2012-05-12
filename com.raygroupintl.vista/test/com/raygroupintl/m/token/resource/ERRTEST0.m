ERRORTST ;
 ;
ADD(A,B) ;
 N SUM
 S SUM=A+B
 Q SUM
 ;
MULTIPLY(A,B) ;
 N PROD
 S PROD=A*
 Q PROD    ; QUIT
 ;
MAIN
 N A,B
 N C,D
 S C=
 S D=A+B
 S MULTIPLY(C,D)
 W D Q ;
 Q
 ; 