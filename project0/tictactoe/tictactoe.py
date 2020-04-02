"""
Tic Tac Toe Player
"""

import math
import copy

X = "X"
O = "O"
EMPTY = None


def initial_state():
    """
    Returns starting state of the board.
    """
    return [[EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY]]


def player(board):
    """
    Returns player who has the next turn on a board.
    """
    X_count = 0
    O_count = 0

    for row in board:
        X_count += row.count(X)
        O_count += row.count(O)

    if X_count <= O_count:
        return X
    else:
        return O


def actions(board):
    """
    Returns set of all possible actions (i, j) available on the board.
    """
    possible_actions = set()

    for i, row in enumerate(board):
        for j, cell in enumerate(row):
            if cell == EMPTY:
                possible_actions.add((i, j))
    
    return possible_actions


def result(board, action):
    """
    Returns the board that results from making move (i, j) on the board.
    """
    if action not in actions(board):
        raise Exception

    # Deep copy used because it's a list of lists
    board_copy = copy.deepcopy(board)
    board_copy[action[0]][action[1]] = player(board)
    return board_copy


def winner(board):
    """
    Returns the winner of the game, if there is one.
    """
    # Check Rows
    for row in board:
        if row[0] != EMPTY and row[0] == row[1] and row[0] == row[2]:
            return row[0]
    
    # Check Columns
    for j in range(3):
        if board[0][j] != EMPTY and board[0][j] == board[1][j]:
            if board[0][j] == board[2][j]:
                return board[0][j]
    
    # Check Diagonals
    if board[1][1] != EMPTY:
        if board[0][0] == board[1][1] and board[0][0] == board[2][2]:
            return board[0][0]
        if board[0][2] == board[1][1] and board[0][2] == board[2][0]:
            return board[0][2]

    return None


def terminal(board):
    """
    Returns True if game is over, False otherwise.
    """
    if winner(board) is None:
        for row in board:
            for cell in row:
                if cell == EMPTY:
                    return False
    
    # Returns true if there's a winner or no empty space
    return True


def utility(board):
    """
    Returns 1 if X has won the game, -1 if O has won, 0 otherwise.
    """
    winning_player = winner(board)

    if winning_player is X:
        return 1
    if winning_player is O:
        return -1
    
    return 0


def minimax(board):
    """
    Returns the optimal action for the current player on the board.
    """
    if terminal(board):
        return None

    max_action_value = -1
    min_action_value = 1
    best_action = None

    for action in actions(board):
        if player(board) == X:
            # Action value given by O player minimizing the action value on next state
            action_value = min_value(result(board, action))
            # Choose action with maximum value
            if action_value > max_action_value:
                best_action = action
                max_action_value = action_value
                # An action with max possible value was reached
                if action_value == 1:
                    return action
        else:
            # Action value given by X player maximizing the action value on next state
            action_value = max_value(result(board, action))
            # Choose action with minimum value
            if action_value < min_action_value:
                best_action = action
                min_action_value = action_value
                # An action with minimum possible value was reached
                if action_value == -1:
                    return action
    
    return best_action

def max_value(board):
    if terminal(board):
        return utility(board)

    value = -1
    for action in actions(board):
        value = max(value, min_value(result(board, action)))
        # An action with max possible value was reached
        if value == 1:
            return value
    
    return value

def min_value(board):
    if terminal(board):
        return utility(board)

    value = 1
    for action in actions(board):
        value = min(value, max_value(result(board, action)))
        # An action with minimum possible value was reached
        if value == -1:
            return value

    return value