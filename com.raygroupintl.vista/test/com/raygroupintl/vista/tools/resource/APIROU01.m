APIROU01 ;AU - API TEST ; JUN 18,2012
 ;;3.0;TESTING;;JUN 18,2012
 ;
 Q
 ;
SUMFACT(N,M)
 N I
 F  D  Q:I>3
 . N R
 . S R=$$FACT^APIROU00(I)+$$SUM^APIROU00(I)
 . S I=I+1
 . S P=R
 Q S
 ;
STORE(A) ;
 N A
 G:A>1 STOREG^APIROU00
 N I
 F I=1:1:10 D
 . S A("F")=$$FACT^APIROU00(I)
 . S D=4
 . S A(D)=4
 Q:K>3
 S R=1
 Q
 ;
