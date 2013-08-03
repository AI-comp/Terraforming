## C
```
$ gcc -v
Using built-in specs.
COLLECT_GCC=gcc
COLLECT_LTO_WRAPPER=/usr/lib/gcc/i686-linux-gnu/4.6/lto-wrapper
Target: i686-linux-gnu
Configured with: ../src/configure -v --with-pkgversion='Ubuntu/Linaro 4.6.3-1ubuntu5' --with-bugurl=file:///usr/share/doc/gcc-4.6/README.Bugs --enable-languages=c,c++,fortran,objc,obj-c++ --prefix=/usr --program-suffix=-4.6 --enable-shared --enable-linker-build-id --with-system-zlib --libexecdir=/usr/lib --without-included-gettext --enable-threads=posix --with-gxx-include-dir=/usr/include/c++/4.6 --libdir=/usr/lib --enable-nls --with-sysroot=/ --enable-clocale=gnu --enable-libstdcxx-debug --enable-libstdcxx-time=yes --enable-gnu-unique-object --enable-plugin --enable-objc-gc --enable-targets=all --disable-werror --with-arch-32=i686 --with-tune=generic --enable-checking=release --build=i686-linux-gnu --host=i686-linux-gnu --target=i686-linux-gnu
Thread model: posix
gcc version 4.6.3 (Ubuntu/Linaro 4.6.3-1ubuntu5) 
```

## C++
```
$ g++ -v
Using built-in specs.
COLLECT_GCC=g++
COLLECT_LTO_WRAPPER=/usr/lib/gcc/i686-linux-gnu/4.6/lto-wrapper
Target: i686-linux-gnu
Configured with: ../src/configure -v --with-pkgversion='Ubuntu/Linaro 4.6.3-1ubuntu5' --with-bugurl=file:///usr/share/doc/gcc-4.6/README.Bugs --enable-languages=c,c++,fortran,objc,obj-c++ --prefix=/usr --program-suffix=-4.6 --enable-shared --enable-linker-build-id --with-system-zlib --libexecdir=/usr/lib --without-included-gettext --enable-threads=posix --with-gxx-include-dir=/usr/include/c++/4.6 --libdir=/usr/lib --enable-nls --with-sysroot=/ --enable-clocale=gnu --enable-libstdcxx-debug --enable-libstdcxx-time=yes --enable-gnu-unique-object --enable-plugin --enable-objc-gc --enable-targets=all --disable-werror --with-arch-32=i686 --with-tune=generic --enable-checking=release --build=i686-linux-gnu --host=i686-linux-gnu --target=i686-linux-gnu
Thread model: posix
gcc version 4.6.3 (Ubuntu/Linaro 4.6.3-1ubuntu5)
```
```
$ clang -v
Ubuntu clang version 3.0-6ubuntu3 (tags/RELEASE_30/final) (based on LLVM 3.0)
Target: i386-pc-linux-gnu
Thread model: posix
```
```
./a.out 
Boost version: 0.198.99
```

## Makefile
```
$ make -v
GNU Make 3.81
Copyright (C) 2006  Free Software Foundation, Inc.
This is free software; see the source for copying conditions.
There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.

This program built for i686-pc-linux-gnu
```

## Java
```
$ java --version
Unrecognized option: --version
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
dockme@ai-comp:~$ java -version
java version "1.7.0_25"
Java(TM) SE Runtime Environment (build 1.7.0_25-b15)
Java HotSpot(TM) Server VM (build 23.25-b01, mixed mode)
```

## Maven
```
$ mvn -v
Apache Maven 3.0.4
Maven home: /usr/share/maven
Java version: 1.7.0_25, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-7-oracle/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.2.0-38-generic-pae-ccs", arch: "i386", family: "unix"
```

## C#
```
$ mono -V
Mono JIT compiler version 2.10.8.1 (Debian 2.10.8.1-1ubuntu2.2)
Copyright (C) 2002-2011 Novell, Inc, Xamarin, Inc and Contributors. www.mono-project.com
        TLS:           __thread
        SIGSEGV:       altstack
        Notifications: epoll
        Architecture:  x86
        Disabled:      none
        Misc:          softdebug 
        LLVM:          supported, not enabled.
        GC:            Included Boehm (with typed GC and Parallel Mark)
```
```
$ dmcs --version
Mono C# compiler version 2.10.8.1
```
```
$ xbuild --verision
XBuild Engine Version 2.10.8.1
Mono, Version 2.10.8.1
Copyright (C) Marek Sieradzki 2005-2008, Novell 2008-2011.
```

## Scala
```
$ scala -version
Scala code runner version 2.9.1 -- Copyright 2002-2011, LAMP/EPFL
```

## Ruby
```
$ ruby -v
ruby 2.0.0p247 (2013-06-27 revision 41674) [i686-linux]
```

## Python
```
$ python -V
Python 2.7.3
```
```
$ python3 -V
Python 3.2.3
```

## PHP
```
$ php5 -version
PHP 5.3.10-1ubuntu3.7 with Suhosin-Patch (cli) (built: Jul 15 2013 18:10:56) 
Copyright (c) 1997-2012 The PHP Group
Zend Engine v2.3.0, Copyright (c) 1998-2012 Zend Technologies
```

## Haskell
```
$ ghc --version
The Glorious Glasgow Haskell Compilation System, version 7.4.1
```
