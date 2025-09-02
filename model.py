{
 "cells": [
  {
   "cell_type": "code",
   "execution_count": 1,
   "id": "5efc57e7",
   "metadata": {},
   "outputs": [
    {
     "name": "stderr",
     "output_type": "stream",
     "text": [
      "2024-01-22 23:06:58.503676: I tensorflow/core/platform/cpu_feature_guard.cc:182] This TensorFlow binary is optimized to use available CPU instructions in performance-critical operations.\n",
      "To enable the following instructions: AVX2 FMA, in other operations, rebuild TensorFlow with the appropriate compiler flags.\n"
     ]
    }
   ],
   "source": [
    "# your_script.py\n",
    "import sys\n",
    "import numpy as np\n",
    "from tensorflow.keras.models import load_model\n",
    "\n",
    "class ModelWrapper:\n",
    "    def __init__(self, model_path):\n",
    "        self.model = load_model(model_path)\n",
    "\n",
    "    def predict(self, input_data):\n",
    "        input_data = np.array(input_data).reshape(1, -1)\n",
    "        prediction = self.model.predict(input_data)\n",
    "        return prediction.tolist()\n",
    "\n",
    "if __name__ == \"__main__\":\n",
    "    model_wrapper = ModelWrapper('my_model200epochscheck.h5')\n",
    "    for line in sys.stdin:\n",
    "        input_data = list(map(float, line.strip().split(',')))\n",
    "        result = model_wrapper.predict(input_data)\n",
    "        print(result)\n",
    "        sys.stdout.flush()\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "id": "4294eb22",
   "metadata": {},
   "outputs": [],
   "source": []
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3 (ipykernel)",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.9.13"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 5
}
