# Tree-shaking drops classes referenced from the base layer of a multi-release jar

With `quarkus.package.jar.tree-shake.mode=classes`, only the `META-INF/versions/11` variant of
`io.vertx.core.impl.LoaderManager` (vertx-core 4.5.35) is analysed. The base variant references
`io.vertx.core.impl.IsolatingClassLoader`, the versioned one does not, so the class is removed
while the base `LoaderManager` stays in the jar.

| vertx-core jar | entries | `IsolatingClassLoader.class` |
|---|---|---|
| original | 963 | present |
| after tree-shaking | 916 | missing |

Default runs work because the JVM picks the `versions/11` variant. Reading the base layer fails.

Quarkus 3.40.1.1, vertx-core 4.5.35, JDK 27.

## Reproduce

```sh
mvn clean package
java -Djdk.util.jar.enableMultiRelease=false -jar target/quarkus-app/quarkus-run.jar
```

```
Caused by: java.lang.NoClassDefFoundError: io/vertx/core/impl/IsolatingClassLoader
        at io.vertx.core.impl.VerticleManager.<init>(VerticleManager.java:35)
        at io.vertx.core.impl.VertxImpl.<init>(VertxImpl.java:230)
        at io.vertx.core.impl.VertxBuilder.vertx(VertxBuilder.java:262)
        at io.quarkus.vertx.core.runtime.VertxCoreRecorder.initialize(VertxCoreRecorder.java:280)
```

With `-Dquarkus.package.jar.tree-shake.mode=none` the same command prints `OK: Vert.x started`.

On Windows PowerShell 5.1, quote the `-D` argument: `java "-Djdk.util.jar.enableMultiRelease=false" -jar ...`.
