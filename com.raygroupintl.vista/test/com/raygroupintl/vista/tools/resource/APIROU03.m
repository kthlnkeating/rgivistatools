APIROU03 ;AU - API TEST ; JUN 18,2012
 ;;3.0;TESTING;;JUN 18,2012
 ;
 Q
 ;
GPIND ;
 S A=B
 G @A
 ;
CALL1
 D B(A,.B) 
 Q
 ;
B(A1,A2)
 W A1
 ;
C ;
 S A2=$G(A2)_"F"
 Q
 ;
