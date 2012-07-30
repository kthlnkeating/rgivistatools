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
FILEMAN
 S DIC="^DIC(9.4,"
 S DIK="^DIE(9.5,"
 S DIE="^DIK(9.6,"
 S DIF="^DIK(9.7,"
 D CHK^FIE("10.9",4)
 D CHK^DIE("10.1",4)
 D CHK^DMI(10.2,4)
 D CHK^DDI(10.3,4)
 Q