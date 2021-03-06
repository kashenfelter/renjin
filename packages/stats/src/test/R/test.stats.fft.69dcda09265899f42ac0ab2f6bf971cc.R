library(hamcrest)

 expected <- c(0x1p+2 + 0x0p+0i, 0x1p+4 + 0x0p+0i, 0x1.cp+4 + 0x0p+0i, 0x1.8p+4 + 0x0p+0i
) 
 

assertThat(stats:::fft(inverse=TRUE,z=c(18+0i, -6+2i, -2+0i, -6-2i))
,  identicalTo( expected, tol = 1e-6 ) )
