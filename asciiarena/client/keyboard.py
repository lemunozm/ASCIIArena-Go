import pynput
import sys
import enum
import threading

class Key(enum.Enum):
    A = enum.auto()
    D = enum.auto()
    S = enum.auto()
    W = enum.auto()
    UNKNOWN = enum.auto()


class Keyboard:
    def __init__(self):
        self._local_key_dict = {}
        self._global_key_dict = {}
        self._global_key_to_remove_list = []
        self._listener = pynput.keyboard.Listener(on_press = self._on_press, on_release = self._on_release)
        self._listener.start()
        self._mutex = threading.Lock()


    def close(self):
        self._listener.stop()


    def is_key_down(self, key):
        return self._local_key_dict.get(key, False)


    def update_key_events(self, key_event_list):
        for key_event in key_event_list:
            key = Keyboard._key_event_to_key(key_event)
            self._local_key_dict[key] = True

        local_key_to_remove_list = []
        with self._mutex:
            for key in self._local_key_dict.keys():
                if not self._global_key_dict.get(key, False):
                    local_key_to_remove_list.append(key)

        for key in local_key_to_remove_list:
            self._local_key_dict.pop(key)


    def _on_press(self, key_code):
        with self._mutex:
            key = Keyboard._key_code_to_key(key_code)
            self._global_key_dict[key] = True


    def _on_release(self, key_code):
        with self._mutex:
            key = Keyboard._key_code_to_key(key_code)
            self._global_key_dict.pop(key)


    @staticmethod
    def _key_event_to_key(key_event):
        switcher = {
            97:  Key.A,
            100: Key.D,
            115: Key.S,
            119: Key.W,
        }
        return switcher.get(key_event, Key.UNKNOWN)


    @staticmethod
    def _key_code_to_key(key_code):
        try:
            switcher_chars = {
                'a': Key.A,
                'd': Key.D,
                's': Key.S,
                'w': Key.W,
            }
            return switcher_chars.get(key_code.char, Key.UNKNOWN)

        except AttributeError:
            return Key.UNKNOWN

