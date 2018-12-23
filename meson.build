project('netorcai-client-java', 'java',
    version: '1.0.0-dev',
    meson_version: '>=0.46.0',
)

src = [
    'src/org/netorcai/message/PlayerInfo.java'
]

classpath = '@0@/deps/json-20180813.jar'.format(meson.source_root())

myjar = jar('netorcai-client-java', src,
    java_args: ['-cp', classpath]
)
