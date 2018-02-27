"""
    Base Adapter Module
"""
from abc import ABCMeta, abstractmethod

class BaseAdapter(metaclass=ABCMeta):
    """
        Base Adapter Class
    """

    app = None
    scope = dict()

    def init(self, app, scope):
        """
            Initialize Function for Adapter
        """
        self.app = app
        self.scope = scope

    @abstractmethod
    def run(self):
        """
            Main Entry for Adapter
        """
        pass
