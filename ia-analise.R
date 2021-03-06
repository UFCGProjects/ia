resultado <- read.csv("~/ufcg/ia/ia/result.csv")
summary(resultado)
#separando por euristica 
resultado_simples <- subset(resultado, resultado$euristica == 'simples')
resultado_otimizado <- subset(resultado, resultado$euristica == 'otimizada')

#Proporcao de viteorias e derrotas de cada euristica
prop.table(table(resultado_simples$win))
prop.table(table(resultado_otimizado$win))

#Grafico de pizza simples
mytable <- prop.table(table(resultado_simples$win))
mytable <- round(mytable, 3)
lbls <- paste(names(mytable), "\n", mytable, sep="")
pie(mytable, labels = lbls, 
    main="Euristica Simples")

#Grafico de pizza otimizado
mytable <- prop.table(table(resultado_otimizado$win))
mytable <- round(mytable, 3)
lbls <- paste(names(mytable), "\n", mytable, sep="")
pie(mytable, labels = lbls, 
    main="Euristica Otimizada")

#Separando entre vitoria e derroa
resultado_simples_win <- subset(resultado_simples, resultado_simples$win == 'true')
resultado_simples_lose <- subset(resultado_simples, resultado_simples$win == 'false')
resultado_otimizado_win <- subset(resultado_otimizado, resultado_otimizado$win == 'true')
resultado_otimizado_lose <- subset(resultado_otimizado, resultado_otimizado$win == 'false')

#Analise de vitorias do Simples
summary(resultado_simples_win)
hist(resultado_simples_win$moves)
#Analise de derrota do Simples
summary(resultado_simples_lose)
hist(resultado_simples_lose$moves)
#Analise de vitorias do Otimizado
summary(resultado_otimizado_win)
hist(resultado_otimizado_win$moves)
#Analise de derrora do Simples
summary(resultado_otimizado_lose)
hist(resultado_otimizado_lose$moves)
