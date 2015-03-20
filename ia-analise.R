simples <- read.csv("~/ufcg/ia/ia/result.csv")
summary(simples)
prop.table(table(simples$win))
mytable <- prop.table(table(simples$win))
mytable <- round(mytable, 3)
lbls <- paste(names(mytable), "\n", mytable, sep="")
pie(mytable, labels = lbls, 
    main="Euristica Simples")

simpleswin <- subset(simples, simples$win == 'true')
simpleslose <- subset(simples, simples$win == 'false')
summary(simpleswin)
summary(simpleslose)
hist(simpleslose$moves)
hist(simpleswin$moves)
















result$movescat <- 'factor'
result$movescat[result$moves <= 10] <- ' 0 - 10'
result$movescat[result$moves > 10 & result$moves <= 23] <- '11 - 23'
result$movescat[result$moves > 23 & result$moves <= 29] <- '24 - 29'
result$movescat[result$moves > 29] <- '30 - 43'
result$movescat <- as.factor(result$movescat)
summary(result)

sort(tapply(result$moves, result$win, mean), decreasing= T)


# Stacked bar graph -- this is probably not what you want
ggplot(data=result, aes(x=win, y=moves, fill=win)) +
  geom_bar(stat="identity") +
  guides(fill=FALSE)

mytable <- prop.table(table(dados_treino_cc_em_tratado$tranccat, dados_treino_cc_em_tratado$codevasao), 1)
mytable <- round(mytable, 3)
print(mytable)
qplot(factor(tranccat), data=dados_treino_cc_em_tratado, geom="bar", fill=codevasao, ylab="Quantidade de alunos", xlab="Tracamentos", main = "Trancamentos e Evasão CC e EM")


prop.table(table(result$movecat))
