RFIOB000 ; AU - ROUTINE FANIN/FANOUT TESTS ; JUN 30, 2013
 ;;3.0;TESTING;; JUN 12,2012
 ;
 Q
 ;
TOPD(A) ;
 I $D(A) D BD0^RFIOB001(A)
 D INTD(A)
 D COMD(A)
 D COMD^RFIOA000(A)
 D T0^RFIOXLLC
 Q
 ;
TOPE(A)
 N SUM,I S SUM=1
 D COMD^RFIOC001(A)
 F I=1:3:1 D
 . Q:$$BE0^RFIOC000(^G(A,I))="N"
 . S SUM=SUM+$$INTE(I)
 . S SUM=SUM+$$COME^RFIOA001(I)
 Q
 ;
FROM2 ;
 Q 
 ;
AE0(A) ;
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