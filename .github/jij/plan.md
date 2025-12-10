## Plan: Stabiliser dwmod + JIJ

Configurer dwmod comme mod séparé embarqué via jar-in-jar, aligner les entrypoints et packager les dépendances Immersive Portals pour un environnement de dev prêt.

### Steps
1. Définir un sourceSet/loom.mods pour dwmod dans build.gradle et inclure src/dwmod/java + src/dwmod/resources/fabric.mod.json dans le jar JIJ.
2. Clarifier l’unique lieu de déclaration d’entrypoint ender.DwMod (soit dans src/main/resources/fabric.mod.json, soit dans src/dwmod/resources/fabric.mod.json) et supprimer les doublons.
3. Paramétrer build.gradle pour générer le JIJ dwmod (tâche remapJar/include ou jarJar) et l’embarquer dans le jar principal Immersive Portals.
4. Mettre à jour les versions/props (mod_version, dwmod version) et s’assurer que les dépendances requises (e.g. dimlib, cloth-config, autres deps IP) sont incluses via include afin qu’elles soient packagées.
5. Vérifier que l’accessWidener et les mixins nécessaires sont visibles pour dwmod (chemins de ressources, duplication EXCLUDE) et ajuster si besoin.

### Further Considerations
1. dwmod doit-il être un mod séparé JIJ (recommandé) ou juste un entrypoint ajouté au mod immersive_portals ?
2. Quelles dépendances supplémentaires devaient être packagées en JIJ en plus de cloth-config et dimlib (ex: modmenu, geckolib, autres) ?
3. Souhaitez-vous que les versions dwmod suivent mod_version ou un numéro indépendant ?
