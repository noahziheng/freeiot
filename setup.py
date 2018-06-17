"""
    Library's Setuptools Script
"""
from setuptools import setup, find_packages
import imp, os

here = os.path.abspath(os.path.dirname(__file__))

PACKAGE_NAME = 'libfreeiot'
VERSION = imp.load_source('version', os.path.join(here, '%s/version.py' % PACKAGE_NAME)).__version__

setup(
    name = "libfreeiot",
    version = VERSION,
    description = 'A free, open-source IoT Framework',
    author = 'Noah Gao',
    author_email = 'noahgaocn@outlook.com',
    url = 'https://github.com/noahziheng/freeiot',
    download_url = 'https://github.com/noahziheng/freeiot/archive/' + VERSION + '.tar.gz',
    packages = find_packages(),
    install_requires=[    # 依赖列表
        'Flask>=0.12.2',
        'Flask-JWT-Simple>=0.0.3',
        'Flask-PyMongo>=0.5.1',
        'Flask-RESTful>=0.3.6',
        'paho-mqtt>=1.3.1',
        'pylint>=1.8.2',
        'python-dotenv>=0.7.1'
    ]
)
