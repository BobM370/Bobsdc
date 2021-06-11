# Bobsdc
Hi
This project is based on an academic paper "Machine learning classification and regression models for predicting directional changes trend reversal in FX markets"
authors - Adesola Adegboye and Michael Kampouridis: January 2021
The paper has a link to github to download the project (as an Eclipse project)
https://github.com/adesolaadegboye?SymbolicRegression

After downloading the project I found there were several problems with the source code
I opened a topic at Big Moose Saloon - Coderanch and received excellent help

General opinion is that the github project is poorly setup

I have taken two separate paths:-
a) cloned the Eclipse project to my machine, where I so far have edited some source code to my liking
b) attempted to rebuild the project under Maven (so away from any IDEs etc)

I am new to Maven and using the command line

As far as the Maven project goes I have temporarily removed the 12 java files under technical anaysis
This is because:-
1) They were simply used for comparison with the paper's results to show this current approach is better
2) they involved eu.verdelhan.ta4j which is defunct and I am trying to 'replace' with org.ta4j

