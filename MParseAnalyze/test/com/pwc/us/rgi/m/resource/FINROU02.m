FINROU02 ; AU - FANIN TEST ; NOV 12,2012
 ;;3.0;TESTING;; NOV 12,2012
 ;
 Q
 ;
ADD
 S LIST=+$G(LIST)+1
 S LIST(LIST)=5
 D ADDALL^FINROU01(.LIST)
 Q
 ;
SUB
 S LIST=+$G(LIST)+1
 S LIST(LIST)=5
 D SUBALL^FINROU01(.LIST)
 Q
 ;
OTHER
 D ADD^FINROU00(RESULT,LIST(I))
 Q 
 ; 
MULT
 S LIST=+$G(LIST)+1
 S LIST(LIST)=5
 D MULTALL^FINROU01(.LIST)
 Q
 ;
SUB2
 S LIST=+$G(LIST)+1
 S LIST(LIST)=4
 G SUB 
 ;
ADD2
 S LIST=+$G(LIST)+1
 S LIST(LIST)=4
 G ADD
 