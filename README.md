Heurística

Hem escollit una heurística que té en compte 3 factors:

El nombre de fitxes i la seva posició al tauler
Els triangles que formen entre elles
Les trampes que poden realitzar al contrari

En tots aquests 3 factors sumem per les nostres fitxes i restem per les altres.

El mètode que hem utilitzat per escollir aquests 3 factors i descartar altres ha sigut el mateix per les 3.
Després d’entendre com funcionava el joc i de jugar moltes partides hem pensat certes posicions que són favorables per nosaltres i a base de provar-les, donant primerament valors arbitraris i més endavant canviant aquests valors hem vist si ens sortia  a compte mantenir aquests factors. 

1. Nombre de fitxes 

Sumem per cada fitxa un valor arbitrari i a més un valor més gran depenent de quant endavant es troben.

Per le reines sumem un valor major i a més un valor més gran depenent de quant enrere es troben (per evitar que les reines es quedin a l’última fila).

2. Triangles 

Per cada triangle o ½ triangle que es forma sumem un valor arbitrari a l’heurística.

Els triangles es poden formar amb dues peces més, és a dir una fitxa adelantada i 2 més, darrera a la dreta i a l’esquerra.
Amb una fitxa a un costat i una paret a l’altre costat.
A part es poden formar ½ triangles amb una fitxa o una paret a qualsevol costat.



3. Trap

Contemplem 3 situacions per cada banda, les explicarem només per una banda:

Quan hi ha una fitxa a cada banda i s’ha d’evitar la doble kill fent un zig-zag, o seguint en diagonal:

Quan només hi ha una filera i per tant hem d’evitar només que faci doble kill quan segueix la mateixa diagonal:
 
Quan la fitxa del darrera es troba a la primera fila i per tant no ens hem de preocupar de que facin doble kill:


COMPARACIÓ IDS amb parametritzat per profunditat.

Per fer aquesta comparació hem decidit agafar 8 com a profunditat que hem de baixar.
El codi de IDS te la optimització posada i hem editat el codi perquè arribi a la profunditat 10 i es surti del bucle, també esta posat que la profunditat on comença sigui 0 i vagi augmentat fins a 10.

La comprovació de temps s’ha fet  manualment, agafant un crono y posant primer el programa amb IDS, i desprès el limitat per profunditat, hem cronometrat el primer moviment perquè l'arbre de moviments posibles sigui el mateix. 

El programa de IDS a trigat 5.14 segons i el limitat en profunditat ha trigat 5,40 segons

PASCUALINHO IDS


Hem implementat el algorisme de zobrist hashing, per retornar  un hash  segons el tauler que tenim. També hem fet que quan hi hagi un moviment  en el tauler, només fem las xors de les peces que s’actualitza i no de tot el tauler. Per tant de optimitzar el màxim el càlcul del hash.

Desprès hem posat en una taula de hash que conté el hash del tauler com a clau i com a valor el index del millor moviment, juntament amb el nivell que recorre, guardem el tauler(el seu hash, el seu millor moviment, i la profunditat que explora) si la profunditat que explorem en el tauler que estem tractant és més gran que la que está guardat en la taula de hash.


Posteriorment, hem fet que si trobem un tauler  igual al que estem tractant(és a dir té el mateix hash). Reordenem la llista de moviments i posem al principi el moviment que tenim guardat com millor moviment en la taula de hash.

En la foto de la esquerra podem veure els nivells que baixa sense la millora, i a la dreta amb la millora feta. Podem concluir que baixa uns 2 o 3 nivells més gràcies a la taula de hash amb el algorisme de zobrist. Las dos captures són en el inici de la partida. on més o menys recorren les mateixes possibilitats de moviments

FET PER: MARC PASCUAL DESENTRE, IVAN GARCIA


