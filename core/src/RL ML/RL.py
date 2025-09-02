import tensorflow as tf




import random
import numpy as np
from keras import layers 
def build_dqn_model(input_shape, num_actions):
    model = tf.keras.Sequential([
        layers.Dense(64, activation='relu', input_shape=input_shape),
        layers.Dense(64, activation='relu'),
        layers.Dense(num_actions, activation='linear')
    ])
    return model
class ReplayBuffer:
    def __init__(self, capacity):
        self.capacity = capacity
        self.buffer = [ ]
        self.position = 0

    def add_experience(self, experience):
        if len(self.buffer) < self.capacity:
            self.buffer.append(experience)
        else:
            self.buffer[self.position] = experience
        self.position = (self.position + 1) % self.capacity

    def sample_batch(self, batch_size):
        return random.sample(self.buffer, batch_size)


class DQNAgent:
    def __init__(self, state_size, action_size, gamma=0.95, epsilon=1.0, epsilon_decay=0.995, epsilon_min=0.01):
        self.state_size = state_size
        self.action_size = action_size
        self.gamma = gamma
        self.epsilon = epsilon
        self.epsilon_decay = epsilon_decay
        self.epsilon_min = epsilon_min
        self.model = build_dqn_model(state_size, action_size)
        self.target_model = build_dqn_model(state_size, action_size)
        self.target_model.set_weights(self.model.get_weights())
        self.replay_buffer = ReplayBuffer(capacity=10000)

    def get_action(self, state):
        if np.random.rand() <= self.epsilon:
            return np.random.choice(self.action_size)
        else:
            q_values = self.model.predict(state)
            return np.argmax(q_values[0])

    def train(self, batch_size=32):
        if len(self.replay_buffer.buffer) < batch_size:
            return

        experiences = self.replay_buffer.sample_batch(batch_size)
        states, actions, rewards, next_states, dones = zip(*experiences)

        states = np.vstack(states)
        next_states = np.vstack(next_states)

        targets = rewards + self.gamma * np.amax(self.target_model.predict(next_states), axis=1) * (1 - dones)

        with tf.GradientTape() as tape:
            q_values = self.model(states, training=True)
            action_masks = tf.one_hot(actions, self.action_size)
            selected_q = tf.reduce_sum(tf.multiply(q_values, action_masks), axis=1)
            loss = tf.reduce_mean(tf.square(targets - selected_q))

        gradients = tape.gradient(loss, self.model.trainable_variables)
        self.model.optimizer.apply_gradients(zip(gradients, self.model.trainable_variables))

        # Update target network periodically
        if episode % update_target_network_frequency == 0:
            self.target_model.set_weights(self.model.get_weights())

        # Decay exploration rate
        if self.epsilon > self.epsilon_min:
            self.epsilon *= self.epsilon_decay
# Define hyperparameters
state_size = 8# Define the size of your state representation CHANGE THIUIISSSSSSSSSSS
action_size = 7# Define the number of possible actions CHANGE THIS ASDGASFGADSFGSDFGSDFGDSFGDSFG
gamma = 0.95
epsilon = 1.0
epsilon_decay = 0.995
epsilon_min = 0.01
update_target_network_frequency = 100  # Update target network every N episodes

# Create DQN agent
dqn_agent = DQNAgent(state_size, action_size, gamma, epsilon, epsilon_decay, epsilon_min)

# Training loop
for episode in range(num_episodes=54):#CHANGEEE THISSSSASDGFASDGFASDGFASDASDF
    state = 8# Initial state of the game]
    total_reward = 0

    while not done:
        action = dqn_agent.get_action(state)
        next_state, reward, done =8 # Execute action in the environment CHANGE THIS OASDNFIKHASBGIKASBGIQS
        dqn_agent.replay_buffer.add_experience((state, action, reward, next_state, done))
        dqn_agent.train()

        state = next_state
        total_reward += reward

    print(f"Episode: {episode + 1}, Total Reward: {total_reward}")

# Save the trained model
dqn_agent.model.save('dqn_model.h5')
