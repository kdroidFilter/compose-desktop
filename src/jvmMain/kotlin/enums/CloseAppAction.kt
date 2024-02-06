package enums

import utils.stringResource

enum class CloseAppAction(val text : String) {
    EXIT(stringResource("app_close_action_quit")),
    BACKGROUND(stringResource("app_close_action_background"))
}