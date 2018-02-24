"""
    Base Adapter Module
"""
from abc import ABCMeta, abstractmethod

class BaseAdapter(metaclass=ABCMeta):
    """
        Base Adapter Class
    """
    @abstractmethod
    def run(self):
        """
            Main Entry for Adapter
        """
        pass
