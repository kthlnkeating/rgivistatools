FINROU01 ; AU - FANIN TEST ; NOV 12,2012
 ;;3.0;TESTING;; NOV 12,2012
 ;
 F I=1:1:2 D
 . D ADD^FINROU00(RESULT,LIST(I))
 ;
 Q
 ;
ADDALL(LIST) ;
 N RESULT
 S RESULT=0
 F I=1:1:LIST D ADD^FINROU00(RESULT,LIST(I))
 Q
 ;
CONDADD(LIST,FLAG) ;
 N RESULT
 S RESULT=0
 I FLAG=1 G DE0
CONDADD2
 F I=1:1:LIST D ADD^FINROU00(RESULT,LIST(I))
 Q
 ;  
DE0 Q
 ;
SUBALL(LIST) ;
 N RESULT
 S RESULT=0
 F I=1:1:LIST D SUB^FINROU00(RESULT,LIST(I))
 Q
 ;
MULTALL(LIST) ;
 N RESULT
 S RESULT=1
 F I=1:1:LIST D MULT^FINROU00(RESULT,LIST(I))
 Q
 ;
MULTAALL(LIST,SCALAR) ;
 N RESULT
 S RESULT=0
 F I=1:1:LIST D MULTADD^FINROU00(RESULT,LIST(I),SCALAR)
 Q
 ;
