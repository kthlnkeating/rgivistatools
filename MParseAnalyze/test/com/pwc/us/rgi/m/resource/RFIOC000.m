RFIOC000 ; AU - ROUTINE FANIN/FANOUT TESTS ; JUN 30, 2013
 ;;3.0;TESTING;; JUN 12,2012
 ;
 Q
 ;
TOPD(A) ;
 I $D(A) D CD0^RFIOC001(A)
 D INTD(A)
 D COMD(A)
 D COMD^RFIOB000(A)
 D T0^RFIOXLLC
 Q
 ;
TOPE(A)
 N SUM,I S SUM=1
 D COMD^RFIOD001(A)
 F I=1:3:1 D
 . Q:$$CE0^RFIOD000(^G(A,I))="N"
 . S SUM=SUM+$$INTE(I)
 . S SUM=SUM+$$COME^RFIOB001(I)
 Q
 ;
FROM2 ;
 Q 
 ;
BE0(A) ;
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