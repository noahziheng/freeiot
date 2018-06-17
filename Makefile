SETUP = python3 setup.py

default: i

i:
	@(python3 -c 'from libfreeiot import version; print("FreeIOT %s" % version.__version__)')

test:
	$(SETUP) test

clean:
	rm -fr build/ dist/ *.egg-info/
	find . | grep __pycache__ | xargs rm -fr
	find . | grep .pyc | xargs rm -f

all: build sdist bdist bdist_egg bdist_wheel

build:
	$(SETUP) build

sdist:
	$(SETUP) sdist

bdist:
	$(SETUP) bdist

bdist_egg:
	$(SETUP) bdist_egg

bdist_wheel:
	$(SETUP) bdist_wheel

install:
	$(SETUP) install --user --prefix=

release:
	$(SETUP) sdist bdist_wheel upload