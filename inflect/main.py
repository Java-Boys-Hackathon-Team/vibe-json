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
    if not words:
        return {"result": ""}

    max_to_inflect = 2  # сколько слов будем склонять
    inflected = []

    for i, word in enumerate(words):
        if i < max_to_inflect:
            parsed = morph.parse(word)
            if parsed:
                form = parsed[0].inflect({case_tag})
                if form:
                    inflected.append(form.word)
                    continue
        inflected.append(word)

    return {"result": " ".join(inflected)}


