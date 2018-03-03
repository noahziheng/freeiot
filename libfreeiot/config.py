"""
    Default Configuration for Flask Config
"""
import os
from dotenv import load_dotenv, find_dotenv

load_dotenv(find_dotenv(usecwd=True), override=True)

class Config:
    """
        Config base class
    """
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'hard to guess string'
    MONGO_DBNAME = os.environ.get('MONGO_DBNAME') or 'test'
    DEBUG = os.environ.get('DEBUG') or True

    @staticmethod
    def init_app(app):
        """
            For Flask Standard
        """
        pass

class DevelopmentConfig(Config):
    """
        Config for debug environment
    """
    DEBUG = True

class TestingConfig(Config):
    """
        Config for test environment
    """
    TESTING = True

class ProductionConfig(Config):
    """
        Config for production environment
    """
    PRODUCTION = True

CONFIG = {
    'development': DevelopmentConfig,
    'testing': TestingConfig,
    'production': ProductionConfig,
    'default': DevelopmentConfig
}
