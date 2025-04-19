from fastapi import FastAPI
from pydantic import BaseModel
from pymorphy2 import MorphAnalyzer

app = FastAPI()
morph = MorphAnalyzer()

CASE_MAP = {
    "именительный": "nomn",
    "родительный": "gent",
    "дательный": "datv",
    "винительный": "accs",
    "творительный": "ablt",
    "предложный": "loct"
}

class PhraseRequest(BaseModel):
    phrase: str
    case: str

@app.post("/inflect")
def inflect_phrase(req: PhraseRequest):
    case_tag = CASE_MAP.get(req.case.lower())
    if not case_tag:
        return {"error": "Неподдерживаемый падеж"}

    words = req.phrase.split()
    inflected_words = []

    for word in words:
        parsed = morph.parse(word)
        if not parsed:
            inflected_words.append(word)
            continue

        best = parsed[0]
        inflected = best.inflect({case_tag})
        if inflected:
            inflected_words.append(inflected.word)
        else:
            inflected_words.append(word)

    return {"result": " ".join(inflected_words)}

