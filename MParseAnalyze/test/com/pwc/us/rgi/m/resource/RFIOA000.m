RFIOA000 ; AU - ROUTINE FANIN/FANOUT TESTS ; JUN 30, 2013
 ;;3.0;TESTING;; JUN 30,2012
 ;
 Q
 ;
TOPD(A) ;
 I $D(A) D AD0^RFIOA001(A)
 D INTD(A)
 D COMD(A)
 D COMD^RFIOD000(A)
 D T0^RFIOXLLC
 Q
 ;
TOPE(A)
 N SUM,I S SUM=1
 D COMD^RFIOB001(A)
 F I=1:3:1 D
 . Q:$$AE0^RFIOB000(^G(A,I))="N"
 . S SUM=SUM+$$INTE(I)
 . S SUM=SUM+$$COME^RFIOD001(I)
 Q
 ;
FROM2 ;
 Q 
 ;
DE0(A) ;
 Q 1
 ;
INTD(A) ;
 Q
 ;
INTE(A) ;
 Q A
 ;
COMD(A) ;
 Q
 ;
COME(A) ;
 Q 1
 ;