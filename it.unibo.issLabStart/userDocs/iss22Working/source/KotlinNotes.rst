.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo


.. _kotlinUnibo: ../../../../../it.unibo.kotlinIntro/userDocs/LabIntroductionToKotlin.html
.. _Scala: https://en.wikipedia.org/wiki/Scala_(programming_language)
.. _Android: https://en.wikipedia.org/wiki/Android_(operating_system)
.. _Kotlin wikipedia: https://en.wikipedia.org/wiki/Kotlin_(programming_language)
.. _Kotlin org: https://kotlinlang.org/
.. _Kotlin Playgound: https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMS42LjIxIiwicGxhdGZvcm0iOiJqYXZhIiwiYXJncyI6IiIsIm5vbmVNYXJrZXJzIjp0cnVlLCJ0aGVtZSI6ImlkZWEiLCJjb2RlIjoiLyoqXG4gKiBZb3UgY2FuIGVkaXQsIHJ1biwgYW5kIHNoYXJlIHRoaXMgY29kZS5cbiAqIHBsYXkua290bGlubGFuZy5vcmdcbiAqL1xuZnVuIG1haW4oKSB7XG4gICAgcHJpbnRsbihcIkhlbGxvLCB3b3JsZCEhIVwiKVxufSJ9
.. _Kotlin Online: https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMS42LjIxIiwicGxhdGZvcm0iOiJqYXZhIiwiYXJncyI6IiIsIm5vbmVNYXJrZXJzIjp0cnVlLCJ0aGVtZSI6ImlkZWEiLCJjb2RlIjoiLyoqXG4gKiBZb3UgY2FuIGVkaXQsIHJ1biwgYW5kIHNoYXJlIHRoaXMgY29kZS5cbiAqIHBsYXkua290bGlubGFuZy5vcmdcbiAqL1xuZnVuIG1haW4oKSB7XG4gICAgcHJpbnRsbihcIkhlbGxvLCB3b3JsZCEhIVwiKVxufSJ9
.. _Kotlin Documentation: https://kotlinlang.org/docs/kotlin-pdf.html
.. _Kotlin Learning materials:  https://kotlinlang.org/docs/learning-materials-overview.html
.. _Get started with Kotlin: https://kotlinlang.org/docs/getting-started.html
.. _Kotlin Basic syntax: https://kotlinlang.org/docs/basic-syntax.html#for-loop



===============================================
KotlinNotes
===============================================

Kotlin nasce nel 2011, per introdurre un nuovo linguaggio per la JVM, ispirato a `Scala`_, con l'oiettivo di una 
compilazione più efficiente. Nel 2020 Kotlin è diventato il linguaggio più usato per lo svluppo di applicazioni Android.

In rete si trova molto materiale relativo a questo linguaggio, che ne permette uno studio efficace. 
Riportiamo qui, per comodità, alcuni riferimenti:

- `Kotlin wikipedia`_: fornisce notizie storiche e dettagli sul linguaggio
- `Kotlin org`_: il sito ufficiale
- `Kotlin Documentation`_: presenta il file pdf della documentazione
- `Get started with Kotlin`_: il sito con tutto quello che c'è da sapere su Kotlin
- `Kotlin Basic syntax`_: panoramica sui costrutti sintattici di Kotlin (parte di `Get started with Kotlin`_)
- `Kotlin Learning materials`_: panoramica sulle risorse utili per lo studio di Koltin (parte di `Get started with Kotlin`_)
- `Kotlin Online`_: permette di eseguire programmi Kotlin
- `kotlinUnibo`_: dispense con esempi Kotlin del corso ISS a.a. 2020-2021

---------------------------------------
Koltin nell'ambito di ISS-72939
---------------------------------------

E' ovvio che la presentazione e lo studio di Kotlin avrebbe bisogno di un congruo numero di ore.

Riportiamo qui i punti salienti di interesse per il corso di Ingegneria dei Sitemi software

- elementi essenziali della  sintassi Kotlin
- *classi ed oggetti* in Kotlin
- il supporto Kotlin allo *stile funzionale* (:blue:`chiusure, callbacks e CPS`)
- il supporto Kotlin alla *programmazione asincrona in stile CPS*
- le :blue:`coroutines` Kotlin come 'thread leggeri' che possono essere sospesi senza bloccare il thread che le esegue
- i Kotlin :blue:`channels` come 'code' che consentono *suspending send* e *suspending receive*
- i Kotlin :blue:`Actors` come supporto al modello degli Attori

Gli obiettivi principali sono:

#. comprendere come la classe AcotrBasic sfrutta i Kotlin Actors per realizzare il supporto QAk
#. fornire informazioni-base utili per scrivere :ref:`CodedQActors` in Kotlin e frasi Kotlin all'interno dei modelli eseguibili QAk.

